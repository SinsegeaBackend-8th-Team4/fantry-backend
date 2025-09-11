package com.eneifour.fantry.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        // 간단한 상태 메시지와 현재 시간을 담은 Map 객체를 생성합니다.
        Map<String, String> response = Map.of(
                "status", "OK",
                "message", "Fantry server is running successfully!"
        );

        // ResponseEntity.ok()를 사용하여 HTTP 200 OK 상태 코드와 함께 JSON 응답을 반환합니다.
        return ResponseEntity.ok(response);
    }
}