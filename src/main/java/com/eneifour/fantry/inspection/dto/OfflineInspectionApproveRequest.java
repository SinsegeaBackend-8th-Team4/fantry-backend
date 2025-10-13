package com.eneifour.fantry.inspection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OfflineInspectionApproveRequest {
    @NotNull(message = "검수자 답변은 필수입니다.")
    private List<InspectorAnswerDto> inspectorAnswers;

    private String inspectionNotes;

    @NotNull(message = "최종 매입가는 필수입니다.")
    @Positive(message = "최종 매입가는 0보다 커야 합니다.")
    private BigDecimal finalBuyPrice;

    private String priceDeductionReason;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class InspectorAnswerDto {
        private String itemKey;
        private String answerValue;
    }
}
