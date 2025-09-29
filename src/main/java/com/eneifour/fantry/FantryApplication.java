package com.eneifour.fantry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FantryApplication {

    public static void main(String[] args) {

        // ✨ 디버깅 코드 시작 ✨
        System.out.println("DEBUG: DB_URL from environment = " + System.getenv("DB_URL"));
        System.out.println("DEBUG: DB_USER from environment = " + System.getenv("DB_USER"));
        System.out.println("DEBUG: DB_PASSWORD from environment = " + System.getenv("DB_PASSWORD"));
        // ✨ 디버깅 코드 끝

        SpringApplication.run(FantryApplication.class, args);
    }

}
