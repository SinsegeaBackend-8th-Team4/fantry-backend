package com.eneifour.fantry.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String errorMessage;

    public ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }
}
