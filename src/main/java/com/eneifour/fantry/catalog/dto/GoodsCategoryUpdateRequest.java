package com.eneifour.fantry.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsCategoryUpdateRequest {
    @NotBlank(message = "카테고리 코드는 필수입니다.")
    private String code;
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;
}
