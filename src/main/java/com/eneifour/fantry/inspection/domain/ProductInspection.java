package com.eneifour.fantry.inspection.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_inspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInspection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productInspectionId;

    @Column(nullable = false, updatable = false, unique = true)
    private String submissionUuid;

    // ** 연관 정보 **
    @Column(nullable = false)
    private int memberId;
    private Integer templateId;
    private Integer templateVersion;
    private Integer inspectorId;

    // ** 상품 기본 정보 **
    @Column(nullable = false)
    private int goodsCategoryId;
    @Column(nullable = false)
    private int artistId;
    private Integer albumId;

    private String itemName;
    @Lob
    private String itemDescription;
    private String hashtags;

    // ** 가격 정보 **
    private BigDecimal expectedPrice;
    private BigDecimal marketAvgPrice;
    private BigDecimal sellerHopePrice;
    private BigDecimal finalBuyPrice; // 최종 매입가

    // ** 배송/계좌 정보 **
    private String shippingAddress;
    private String shippingAddressDetail;
    private String bankName;
    private String bankAccount;

    // ** 상태 정보 **
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InspectionStatus inspectionStatus = InspectionStatus.DRAFT;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus inventoryStatus = InventoryStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @Enumerated(EnumType.STRING)
    private FinalGrade finalGrade;

    // ** 검수 결과 정보 **
    @Lob
    private String inspectionSummary; // 검수 요약
    @Lob
    private String priceDeductionReason; // 가격 차감 사유
    @Lob
    private String inspectionNotes; //내부 검수 메모

    // ** 타임 스탬프 **
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime onlineInspectedAt;
    private LocalDateTime offlineInspectedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;

    // ** 연관 관계 **
    @Builder.Default
    @OneToMany(mappedBy = "productInspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionFile> files = new ArrayList<>();

    // ** Enum 정의 **
    public enum SourceType { CONSIGNMENT, PURCHASED }
    public enum FinalGrade { A, B, C }
}
