package com.eneifour.fantry.bid.service;

import com.eneifour.fantry.bid.dto.BidSummaryResponse;
import com.eneifour.fantry.auction.exception.*;
import com.eneifour.fantry.bid.exception.BidException;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.bid.dto.BidRequest;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.bid.repository.BidRepository;

import com.eneifour.fantry.notification.service.SseConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {

    //의존성 주입
    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final JpaMemberRepository memberRepository;
    private final RedisScript<String> placeBidScript;
    private final BlockingQueue<Bid> bidLogQueue;
    private final BidDbFallbackHandler bidDbFallbackHandler;
    private final BidActionHelper bidActionHelper;
    private final SseConnectionService sseConnectionService;
    // --- 상태 플래그 및 상수 ---
    private final AtomicBoolean isDbFallbackMode = new AtomicBoolean(false); // DB <-> Redis 동기화 및 자기 치유 로직을 위한 상태 플래그
    private static final int MIN_BID_INCREMENT = 1000; // 최소 입찰 증가액 (1,000원) 상수화


    /*---------------------------------------------------------------------------------------*/
    /* 입찰처리 메인 진입점 메서드 -> Redis 우선 사용 ,
     * Redis 서버 장애 발생 시 DB Fallback 로직을 호출하는 Dispatcher 역할 수행
     *@param auctionId 경매 ID
     * @param bidDTO 입찰 정보 DTO
    */
    /*---------------------------------------------------------------------------------------*/
    @Transactional
    public void placeBid(int auctionId , BidRequest bidRequest){

        //1. 공통 사전 검증 로직 (판매 상품 조회 , 상품 상태 조회 , 입찰 형식 검증_100원단위 , 1000원이상 등)
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));
        if (auction.getSaleStatus() != SaleStatus.ACTIVE) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE);
        }

        if (LocalDateTime.now().isAfter(auction.getEndTime())) {
            throw new AuctionException(ErrorCode.AUCTION_NOT_ACTIVE); // 2차 방어
        }

        validateBidBasics(bidRequest.getBidAmount());

        Member bidder = memberRepository.findById(bidRequest.getMemberId())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        try{
            /* 2. 자기 치유 (Redis <-> DB 동기화 )
            * isDbFallbackMode 의 flag로 해당 로직 진입 여부 판단
            */
            if (this.isDbFallbackMode.get()) {
                log.info("System is recovering from DB fallback mode. Re-syncing Redis data for auctionId: {}", auctionId);
                resyncRedisWithDB(auction);
                this.isDbFallbackMode.set(false); // 정상 모드로 전환
                log.info("System is back to normal mode.");
            }

            // [정상 경로] 3. Redis Lua 스크립트를 사용한 원자적 연산 입찰
            int bidAmount = bidRequest.getBidAmount();
            String redisKey = "auction:highest_bid:" + auctionId;

            String result = (String)redisTemplate.execute(
                    placeBidScript,                             // 실행할 스크립트
                    Collections.singletonList(redisKey),        // KEYS[1]
                    String.valueOf(bidAmount),                  // ARGV[1] 새로 들어온 입찰가 (bidAmount)
                    String.valueOf(auction.getStartPrice()),     // ARGV[2] 경매 시작가 (startPrice)
                    String.valueOf(MIN_BID_INCREMENT)       //ARGV[3] 최소 입찰 증가액 (minIncrement)
            );

            // 4. Lua 스크립트 결과에 따른 상세 예외 처리
            handleLuaScriptResult(result, auction);
            log.info("Bid placed successfully via Redis for auctionId {}. Result: {}", auctionId, result);

            // 5. 성공 후 후처리 (브로드캐스팅 및 큐에 추가)
            postBidSuccessActions(auction, bidRequest);

        }catch(DataAccessException e){

            // [Redis 서버 장애 경로] 6. Redis 예외 발생 시 DB Fallback 로직 호출
            log.warn("Redis connection failed for auction_id {}. Falling back to DB for validation.", auctionId, e);
            this.isDbFallbackMode.set(true); // 비상모드 플래그 활성화

            // [수정] 예외를 다시 던져서 GlobalExceptionHandler가 최종 처리하도록 구조 변경
            try {
                bidDbFallbackHandler.placeBidWithDBLock(auction, bidRequest);
            } catch (BusinessException be) {
                throw be; // 비즈니스 예외는 그대로 다시 던짐
            } catch (Exception ex) {
                log.error("Error during DB fallback execution for auctionId: {}", auctionId, ex);
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

        }

    }

    /**
     * [정상 경로] 성공 후 후처리: 브로드캐스팅 및 In-Memory Queue에 추가
     */
    private void postBidSuccessActions(Auction auction, BidRequest bidRequest) {
        int bidAmount = bidRequest.getBidAmount();

        log.debug(bidRequest.toString());

        Member bidder = memberRepository.findById(bidRequest.getMemberId())
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        bidActionHelper.broadcastNewBid(auction.getAuctionId(), bidder, bidAmount);
        sseConnectionService.broadcastToAuctionSubscribersExcludingUser(auction.getAuctionId(), bidder.getId(), "\""+auction.getProductInspection().getItemName()+"\""+" 상품 경매 입찰가가 갱신되었습니다.");

        Bid bidToLog = bidActionHelper.createBidLog(auction, bidder, bidAmount);
        if (!bidLogQueue.offer(bidToLog)) {
            log.error("Failed to add bid log to the queue. Queue might be full. Bid details: {}", bidToLog);
        }
    }

    /**
     * 가장 기본적인 입찰 유효성을 검사합니다. (Redis/DB 공통)
     */
    private void validateBidBasics(int bidAmount) {
        if (bidAmount <= 0) {
            throw new BidException(ErrorCode.BID_AMOUNT_INVALID);
        }
        if (bidAmount % 100 != 0) {
            throw new BidException(ErrorCode.BID_UNIT_INVALID);
        }
    }

    /**
     * Lua 스크립트의 반환 결과를 해석하여 적절한 예외를 발생시킵니다.
     */
    private void handleLuaScriptResult(String result, Auction auction) {
        if (result == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        if (result.startsWith("SUCCESS")) {
            return; // 성공
        }
        if (result.startsWith("ERROR:")) {
            log.error("Lua script execution error for auctionId {}: {}", auction.getAuctionId(), result);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        switch (result) {
            case "FAILURE_TOO_LOW_START":
                throw new BidException(ErrorCode.BID_TOO_LOW_START);
            case "FAILURE_TOO_LOW_INCREMENT":
                throw new BidException(ErrorCode.BID_TOO_LOW_INCREMENT);
            default:
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Redis 장애 복구 시, DB 기준으로 Redis 데이터를 동기화합니다.
     */
    private void resyncRedisWithDB(Auction auction) {
        String redisKey = "auction:highest_bid:" + auction.getAuctionId();
        Optional<Bid> dbTopBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auction.getAuctionId());

        if (dbTopBidOptional.isPresent()) {
            int dbHighestBid = dbTopBidOptional.get().getBidAmount();
            redisTemplate.opsForValue().set(redisKey, String.valueOf(dbHighestBid));
            log.info("Redis key '{}' synced with DB value {}.", redisKey, dbHighestBid);
        } else {
            // DB에도 입찰 기록이 없다면 Redis 키를 삭제하여 초기화
            redisTemplate.delete(redisKey);
            log.info("Redis key '{}' deleted as no bids found in DB.", redisKey);
        }
    }

    /*---------------------------------------------------------------------------------------*/
    // 기본 CRUD 및 Log 조회
    /*---------------------------------------------------------------------------------------*/
    //전체 Log 조회
    public List<Bid> findAll(){
        return bidRepository.findAll();
    }

    //Member_id 기준으로 Log 조회
    public List<Bid> findByBidderId(int bidder_id){
        return bidRepository.findByBidderId(bidder_id);
    }

    //Item_id 기준으로 Log 조회
    public List<BidSummaryResponse> findByItemId(int itemId){
        List<Bid> bidList = bidRepository.findByItemIdOrderByBidAmountDesc(itemId);
        return bidList.stream() // bidList를 스트림으로 변환
                .map(bid -> BidSummaryResponse.builder() // 각 bid 객체를 BidPublicResponse 객체로 매핑(변환)
                        .bidAmount(bid.getBidAmount()) // bid 객체에서 입찰 금액을 가져옴
                        .bidAt(bid.getBidAt())     // bid 객체에서 생성 시간을 가져옴 (엔티티의 필드명에 따라 getBidAt() 등이 될 수 있습니다)
                        .build())
                .collect(Collectors.toList());
    }

    //Member_id 및 Item_id 기준으로 Log 조회
    public List<Bid> findByBidderIdAndItemId(int bidderId, int itemId){
        return bidRepository.findByBidderIdAndItemId(bidderId,itemId);
    }

    //Log update
    public void update(Bid bid){
        bidRepository.save(bid);
    }

    //Log delete
    public void delete(Bid bid){
        bidRepository.delete(bid);
    }

}
