package com.eneifour.fantry.inspection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InspectionRejectRequest {
    @NotBlank(message = "반려 사유를 입력해주세요.")
    private String rejectionReason;
}
