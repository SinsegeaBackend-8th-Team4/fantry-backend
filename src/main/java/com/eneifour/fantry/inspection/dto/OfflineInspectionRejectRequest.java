package com.eneifour.fantry.inspection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OfflineInspectionRejectRequest {
    @NotBlank(message = "반려 사유는 필수입니다.")
    private String rejectionReason;

    @NotNull(message = "검수자 답변은 필수입니다.")
    private List<OfflineInspectionApproveRequest.InspectorAnswerDto> inspectorAnswers;
}
