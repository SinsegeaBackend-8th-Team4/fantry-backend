package com.eneifour.fantry.checklist.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class OfflineChecklistItemResponse {
    private String itemKey;
    private String itemLabel;
    private String sellerAnswer; // 판매자 답변
    private String inspectorAnswer; // 검수자 답변
    private String note; // 불일치 사유
}
