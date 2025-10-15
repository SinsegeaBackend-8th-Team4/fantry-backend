package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.bid.domain.Bid;
import com.eneifour.fantry.bid.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final BidRepository bidRepository;
    private final RedisTemplate redisTemplate;

    //Redis 최고가 및 DB 최고가 교차 검증 (start price 및 auction id 필요.)
    @Transactional
    public int getCurrentPrice(int startPrice, int auctionId){
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
                currentPrice = topBidOptional.map(Bid::getBidAmount).orElse(startPrice);

                if(currentPrice != startPrice){

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
            currentPrice = topBidOptional.map(Bid::getBidAmount).orElse(startPrice);
        }
        return  currentPrice;
    }

    @Transactional(readOnly = true)
    public int getHighestBidderId(int auctionId) {
        int bidderId = 0;
        String redisKey = "auction:highest_bidder_id:" + auctionId;

        try {
            Object redisValue = redisTemplate.opsForValue().get(redisKey);
            String bidderIdStr = (redisValue != null) ? redisValue.toString() : null;

            if (bidderIdStr != null) {
                bidderId = Integer.parseInt(bidderIdStr);
            } else {
                // --- Redis에 값이 없는 경우 (Cache Miss) ---
                Optional<Bid> topBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auctionId);

                if (topBidOptional.isPresent()) {
                    bidderId = topBidOptional.get().getBidderId();
                }else{
                    bidderId = 0;
                }
                // 입찰 내역이 DB에도 없으면 bidderId는 그대로 null 입니다.
            }
        } catch (DataAccessException e) {
            // --- [Redis 서버 장애 경로] Redis 연결 실패 시 DB로 Fallback ---
            Optional<Bid> topBidOptional = bidRepository.findTopByItemIdOrderByBidAmountDesc(auctionId);
            bidderId = topBidOptional.map(Bid::getBidderId).orElse(0);
        }
        return bidderId;
    }
}
