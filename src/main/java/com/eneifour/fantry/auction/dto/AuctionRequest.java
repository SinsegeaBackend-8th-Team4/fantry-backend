package com.eneifour.fantry.auction.dto;

import com.eneifour.fantry.auction.domain.SaleType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 경매 생성 및 수정을 위한 요청 DTO.
 * @Valid 어노테이션을 통해 Controller 단에서 입력값 검증을 수행합니다.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionRequest {

    // ProductInspection과 연관된 ID
    @NotNull(message = "상품 검수 ID는 필수입니다.")
    private int productInspectionId;

    @NotNull(message = "판매 타입을 지정해야 합니다.")
    private SaleType saleType;

    @NotNull(message = "시작가를 입력해야 합니다.")
    @Min(value = 100, message = "시작가는 100원 이상이어야 합니다.")
    private Integer startPrice;

    @NotNull(message = "경매 및 판매 시작 시간은 필수입니다.")
    private LocalDateTime startTime;

    @NotNull(message = "경매 종료 시간은 필수입니다.")
    private LocalDateTime endTime;

    private Boolean isReRegistration;
}