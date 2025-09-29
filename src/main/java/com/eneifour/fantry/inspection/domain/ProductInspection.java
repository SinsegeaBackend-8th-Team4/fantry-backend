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

    private String submissionUuid;

    private int memberId;
    private Integer templateId;
    private Integer templateVersion;

    private int goodsCategoryId;
    private int artistId;
    private Integer albumId;

    private String itemName;
    private String itemDescription;
    private String hashtags;

    private BigDecimal expectedPrice;
    private BigDecimal marketAvgPrice;
    private BigDecimal sellerHopePrice;
    private BigDecimal finalBuyPrice;

    private String shippingAddress;
    private String shippingAddressDetail;
    private String bankName;
    private String bankAccount;

    @Enumerated(EnumType.STRING)
    private InspectionStatus inspectionStatus = InspectionStatus.DRAFT;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus = InventoryStatus.PENDING;
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    private Integer inspectorId;

    @Enumerated(EnumType.STRING)
    private FinalGrade finalGrade;

    @Lob
    private String inspectionSummary;

    @Lob
    private String priceDeductionReason;

    @Lob
    private String inspectionNotes;

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime onlineInspectedAt;
    private LocalDateTime offlineInspectedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productInspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionFile> files = new ArrayList<>();

    public enum InspectionStatus { DRAFT, SUBMITTED, FIRST_REVIEWED, OFFLINE_INSPECTING, COMPLETED, REJECTED }
    public enum InventoryStatus { PENDING, ACTIVE, SOLD, CANCELED }
    public enum SourceType { CONSIGNMENT, PURCHASED }
    public enum FinalGrade { A, B, C }
}
