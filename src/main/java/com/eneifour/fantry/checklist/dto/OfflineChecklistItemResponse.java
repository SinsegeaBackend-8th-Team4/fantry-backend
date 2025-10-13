package com.eneifour.fantry.checklist.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfflineChecklistItemResponse {
    // 체크리스트 항목 정의 정보
    private OnlineChecklistItemResponse checklistItem;

    // 답변 정보
    private String sellerAnswer;
    private String inspectorAnswer;
    private String note;
}
