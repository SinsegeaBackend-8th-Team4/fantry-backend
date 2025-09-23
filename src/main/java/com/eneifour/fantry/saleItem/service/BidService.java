package com.eneifour.fantry.saleItem.service;

import com.eneifour.fantry.saleItem.domain.Auction;
import com.eneifour.fantry.saleItem.domain.Bid;
import com.eneifour.fantry.saleItem.domain.Member;
import com.eneifour.fantry.saleItem.domain.SaleStatus;
import com.eneifour.fantry.saleItem.dto.BidDTO;
import com.eneifour.fantry.saleItem.exception.AuctionException;
import com.eneifour.fantry.saleItem.exception.BidException;
import com.eneifour.fantry.saleItem.exception.MemberException;
import com.eneifour.fantry.saleItem.repository.AuctionRepository;
import com.eneifour.fantry.saleItem.repository.BidRepository;
import com.eneifour.fantry.saleItem.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /*---------------------------------------------------------------------------------------*/
    // Redis 및 BroadCast 활용한 경매 상품 입찰 로직
    /*---------------------------------------------------------------------------------------*/
    //Redis
    @Transactional
    public void placeBid(int auctionId , BidDTO bidDTO){

        //1. 판매 상품 정보 조회 및 예외 처리
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(()->
                        new AuctionException("존재하지 않는 경매 상품 입니다."));
        if(auction.getSaleStatus() != SaleStatus.ACTIVE){
            throw new BidException("현재 진행 중인 경매가 아닙니다");
        }

        //2. Redis에서 현재 최고 입찰가 조회
        String redisKey = "auction:highest_bid:" + auctionId;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String currentHighestBidStr = ops.get(redisKey);

        int bidAmount = bidDTO.getBidAmount();

        //3. 입찰가 검증 로직
        if(currentHighestBidStr != null) {
            //Redis 에 값이 있는 경우 (입찰 중인 상품인 경우)
            int currentHighestBid = Integer.parseInt(currentHighestBidStr);
            //현재 입찰가 보다 낮은 금액 입력 시
            if (bidAmount <= currentHighestBid) {
                throw new BidException("현재 최고 입찰가( " + currentHighestBid + " 원) 보다 높은 금액을 입력하세요");
            }
        }else{
            //Redis 에 값이 없는 경우 (첫 입찰인 경우)
            //경매 시작가 보다 낮은 입찰 금액 입력 시
            int startPrice = auction.getStartPrice();
            if(bidAmount <= startPrice){
                throw new BidException("경매 시작가( "+startPrice+" 원) 보다 높은 금액을 입력하세요");
            }
        }

        //4. redis에 최고 입찰가 업데이트
        ops.set(redisKey, String.valueOf(bidAmount));
        log.debug("Redis 업데이트 auction {}: 신규 입찰가 {}",auctionId,bidAmount);

        //5. 신규 입찰 정보 브로드캐스팅
        Member bidder = memberRepository.findById(bidDTO.getMemberId())
                .orElseThrow(() -> new MemberException("존재하지 않는 회원입니다."));

        simpMessagingTemplate.convertAndSend("/topic/auctions/"+auctionId, Map.of(
                "newPrice",bidAmount,
                "memberId",bidDTO.getMemberId()
        ));

        // 6. DB 입찰 기록 비동기 저장
        // 트래픽 감당 및 실시간 입찰 처리 = 메모리를 이용한 Redis
        //  Lg 저장을 위한 DB 연결 = JPA + Mysql
        String itemName = auction.getInventoryItem().getItemName();
        saveBidAsync(auctionId,bidDTO.getMemberId(),bidAmount,itemName);

    }

    @Async
    @Transactional
    public void saveBidAsync(int auctionId , int memberId , int bidAmount, String itemName){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("비동기 저장 중 회원 정보를 찾을 수 없습니다."));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException("비동기 저장 중 경매 정보를 찾을 수 없습니다."));

        if(auction != null && member  != null){
            Bid bid = new Bid();
            bid.setBidAmount(bidAmount);
            bid.setItemId(auctionId);
            bid.setBidderId(memberId);
            bid.setBidderName(member.getName());
            bid.setItemName(itemName);
            bidRepository.save(bid);
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
    public List<Bid> findByItemId(int itemId){
        return bidRepository.findByItemId(itemId);
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
