package com.eneifour.fantry.inspection.support.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.OffsetDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionApiResponse<T> {
    private boolean success;          // 항상 true (에러는 별도 ErrorResponse 사용)
    private String code;              // "OK"
    private String message;           // "success"
    private T data;                   // 실데이터
    private OffsetDateTime timestamp; // 서버 시간

    public static <T> InspectionApiResponse<T> ok(T data) {
        return InspectionApiResponse.<T>builder()
                .success(true)
                .code("OK")
                .message("success")
                .data(data)
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
