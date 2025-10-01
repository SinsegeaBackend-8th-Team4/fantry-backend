package com.eneifour.fantry.auction.service;

import com.eneifour.fantry.auction.domain.Auction;
import com.eneifour.fantry.auction.domain.SaleStatus;
import com.eneifour.fantry.auction.domain.SaleType;
import com.eneifour.fantry.auction.dto.AuctionDetailDTO;
import com.eneifour.fantry.auction.exception.AuctionException;
import com.eneifour.fantry.auction.exception.ErrorCode;
import com.eneifour.fantry.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

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
    public AuctionDetailDTO  findOne(int auctionId){
        AuctionDetailDTO auctionDetail = auctionRepository.findAuctionDetailById(auctionId)
                .orElseThrow(() -> new AuctionException(ErrorCode.AUCTION_NOT_FOUND));

        return auctionDetail;
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
