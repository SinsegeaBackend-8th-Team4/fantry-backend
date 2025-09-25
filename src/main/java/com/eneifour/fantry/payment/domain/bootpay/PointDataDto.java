package com.eneifour.fantry.payment.domain.bootpay;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointDataDto {
    @JsonAlias("tid")
    protected String tid;
}
