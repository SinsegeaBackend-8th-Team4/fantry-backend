package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.Bid;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailResponse;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import com.eneifour.fantry.auction.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final RedisTemplate redisTemplate;

    //전체 내역 조회
    public List<Auction> findAll(){
        return auctionRepository.findAll();
    }

    //Sale_type 에 따른 상품 내역 조회
    public List<Auction> findBySaleType(SaleType saleType){
        return auctionRepository.findBySaleType(saleType);
    }

    //Sale_Status 에 따른 상품 내역 조회
    public List<Auction> findBySaleStatus(SaleStatus saleStatus){
        return auctionRepository.findBySaleStatus(saleStatus);
    }

    //Sale_type 및 Sale_status 에 따른 상품 내역 조회
    public List<Auction> findBySaleTypeAndSaleStatus(SaleType saleType, SaleStatus saleStatus){
        return auctionRepository.findBySaleTypeAndSaleStatus(saleType, saleStatus);
    }

    //Auction_id 를 이용한 1건 조회
    @Transactional
    public AuctionDetailResponse findOne(int auctionId){
        // --- 1단계: 기본 정보 조회 (DB) ---
        AuctionDetailResponse baseDetail = auctionRepository.findAuctionDetailById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        // --- 2단계: 현재 최고 입찰가 결정 (Redis 우선 조회) ---
        int currentPrice;
        String redisKey = "auction:highest_bid:" + auctionId;

        try{
            Object redisValue = redisTemplate.opsForValue().get(redisKey);
            String redisPriceStr = (redisValue != null) ? redisValue.toString() : null;

            if (redisPriceStr != null){
                // --- 2-1. Redis 에 값이 있는 경우 (가장 일반적인 경우) ---
                // -> Redis 의 값을 현재가로 사용
                currentPrice = Integer.parseInt(redisPriceStr);
            }else{
                // --- 2-2. Redis에 값이 없는 경우 (Cache Miss 인지 , 첫 입찰인지 분기)
                Optional<Bid> topBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auctionId);
                currentPrice = topBidOptional.map(Bid::getBidAmount).orElse(baseDetail.getStartPrice());

                if(currentPrice != baseDetail.getStartPrice()){

                    try {
                        redisTemplate.opsForValue().set(redisKey, String.valueOf(currentPrice));
                    } catch (DataAccessException e) {
                        log.warn("Redis write failed during cache population for auctionId: {}. Read operation will proceed.", auctionId);
                    }
                }
            }

        }catch(DataAccessException e){
            // --- [Redis 서버 장애 경로] Redis 연결 실패 시 DB로 Fallback ---
            log.warn("Redis connection failed for auctionId: {}. Falling back to DB for current price.", auctionId, e);
            Optional<Bid> topBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auctionId);
            currentPrice = topBidOptional.map(Bid::getBidAmount).orElse(baseDetail.getStartPrice());
        }

        return AuctionDetailResponse.builder()
                // --- 기본 정보 복사 ---
                .auctionId(baseDetail.getAuctionId())
                .saleStatus(baseDetail.getSaleStatus())
                .saleType(baseDetail.getSaleType())
                .startTime(baseDetail.getStartTime())
                .endTime(baseDetail.getEndTime())
                .memberId(baseDetail.getMemberId())
                .itemName(baseDetail.getItemName())
                .itemDescription(baseDetail.getItemDescription())
                .categoryName(baseDetail.getCategoryName())
                .artistName(baseDetail.getArtistName())
                .albumTitle(baseDetail.getAlbumTitle())
                .startPrice(baseDetail.getStartPrice())
                // --- 입찰 정보 채우기 (분기 처리) ---
                .currentPrice(currentPrice)
                .build();
    }

    // 판매 상품 중 , member_id 를 기준으로 sale_status가 특정한 것 조회
    public List<Auction> findBymemberIdAndSaleStatus(int memberId, SaleStatus saleStatus){
        return auctionRepository.findByProductInspection_MemberIdAndSaleStatus(memberId, saleStatus);
    }

    // 판매 상품 중 , member_id 를 기준으로 모든 상품 조회
    public List<Auction> findBymemberId(int memberId){
        return auctionRepository.findByProductInspection_MemberId(memberId);
    }

    //판매 상품 1건 등록 
    public void save(Auction auction){
        auctionRepository.save(auction);
    }
    
    //판매 상품 수정
    public void update(Auction auction){
        auctionRepository.save(auction);
    }
    
    //판매 상품 삭제
    public void delete(Auction auction){
        auctionRepository.delete(auction);
    }


}
