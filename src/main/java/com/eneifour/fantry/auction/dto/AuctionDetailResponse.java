package com.eneifour.fantry.auction.dto;

import com.eneifour.fantry.common.util.file.FileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuctionDetailResponse {
    //기본 경매 or 판매 정보
    private int auctionId;
    private String saleStatus;
    private String saleType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //상품 정보
    private int productInspectionId;
    private int memberId;
    private String itemName;
    private String itemDescription;
    private String hashtags;
    private String categoryName;
    private String artistName;
    private String artistGroupType;
    private String albumTitle;

    //상품 사진 파일
    private List<FileInfo> fileInfos;

    @Getter
    @NoArgsConstructor
    public static class FileInfo {
        private int fileId;
        private String fileUrl;
        private String fileType;

        // JPQL 구문 생성자
        public FileInfo(int fileId, String storedFilePath, FileType fileTypeEnum) {
            this.fileId = fileId;
            this.fileUrl = storedFilePath;
            this.fileType = fileTypeEnum.toString();
        }
    }

    //가격 정보
    private int startPrice;
    private int currentPrice;

    //최고 입찰자 정보
    private int highestBidderId;

    // current Price 는 bid 에서 조회해야 하므로 , 생성자엔 해당 인자 없이 생성
    public AuctionDetailResponse(
            int auctionId,
            int productInspectionId,
            int memberId,
            int startPrice,
            String saleStatus,
            String saleType,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String itemName,
            String itemDescription,
            String hashtags,
            String categoryName,
            String artistName,
            String artistGroupType,
            String albumTitle
    ) {
        this.auctionId = auctionId;
        this.productInspectionId = productInspectionId;
        this.memberId = memberId;
        this.startPrice = startPrice;
        this.saleStatus = saleStatus;
        this.saleType = saleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.hashtags = hashtags;
        this.categoryName = categoryName;
        this.artistName = artistName;
        this.artistGroupType = artistGroupType;
        this.albumTitle = albumTitle;
    }

}
