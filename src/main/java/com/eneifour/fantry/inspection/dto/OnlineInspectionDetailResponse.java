package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.common.util.file.FileType;
import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * FE에서 관리자 페이지의 1차 온라인 검수 상세 조회 응답 DTO
 */
@Getter
@NoArgsConstructor
public class OnlineInspectionDetailResponse {
    // ** 기본 정보 **
    private int productInspectionId;
    private String submissionUuid;
    private InspectionStatus inspectionStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;

    // ** 상품 정보 **
    private String itemName;
    private String itemDescription;
    private String hashtags;
    private int goodsCategoryId;
    private String goodsCategoryName;
    private int artistId;
    private String artistName;
    private Integer albumId;
    private String albumTitle;

    // ** 가격 정보 **
    private BigDecimal expectedPrice;
    private BigDecimal marketAvgPrice;
    private BigDecimal sellerHopePrice;

    // ** 판매자 정보 **
    private UserInfo seller;
    private String bankName;
    private String bankAccount;
    private String shippingAddress;
    private String shippingAddressDetail;

    // ** 검수자 정보 **
    private UserInfo firstInspector;

    // ** 첨부 파일 정보 **
    @Setter
    private List<FileInfo> files;
    
    // ** 체크리스트 답변 정보 **
    @Setter
    private List<ChecklistAnswerInfo> answers;

    // ** 템플릿 정보 **
    private Integer templateId;
    private Integer templateVersion;

    // ** 1차 반려 사유 필드 **
    private String firstRejectionReason;
    
    // JPQL 프로젝션을 위한 생성자
    @Builder
    public OnlineInspectionDetailResponse(int productInspectionId, String submissionUuid, InspectionStatus inspectionStatus, LocalDateTime submittedAt, String itemName, String itemDescription, String hashtags, int goodsCategoryId, String goodsCategoryName, int artistId, String artistName, Integer albumId, String albumTitle, BigDecimal expectedPrice, BigDecimal marketAvgPrice, BigDecimal sellerHopePrice, UserInfo seller, String bankName, String bankAccount, String shippingAddress, String shippingAddressDetail, Integer templateId, Integer templateVersion, UserInfo firstInspector, String firstRejectionReason) {
        this.productInspectionId = productInspectionId;
        this.submissionUuid = submissionUuid;
        this.inspectionStatus = inspectionStatus;
        this.submittedAt = submittedAt;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.hashtags = hashtags;
        this.goodsCategoryId = goodsCategoryId;
        this.goodsCategoryName = goodsCategoryName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumTitle = albumTitle;
        this.expectedPrice = expectedPrice;
        this.marketAvgPrice = marketAvgPrice;
        this.sellerHopePrice = sellerHopePrice;
        this.seller = seller;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.shippingAddress = shippingAddress;
        this.shippingAddressDetail = shippingAddressDetail;
        this.templateId = templateId;
        this.templateVersion = templateVersion;
        this.firstInspector = firstInspector;
        this.firstRejectionReason = firstRejectionReason;
    }

    // 파일 정보
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

    // 체크리스트 답변 정보
    @Getter
    @NoArgsConstructor
    public static class ChecklistAnswerInfo {
        private String itemKey;
        private String itemLabel;
        private String answerValue;
        private String note;

        @Builder
        public ChecklistAnswerInfo(String itemKey, String itemLabel, String answerValue, String note) {
            this.itemKey = itemKey;
            this.itemLabel = itemLabel;
            this.answerValue = answerValue;
            this.note = note;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UserInfo {
        private Integer id;
        private String name;
        private String email;
        private String tel;

        public UserInfo(Integer id, String name, String email, String tel) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.tel = tel;
        }
    }
}
