package com.eneifour.fantry.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisconnectResponse {
    private String message;
    private String username;
    private Integer unsubscribedCount;
}
