package com.eneifour.fantry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class FantryApplication {

    public static void main(String[] args) {
        SpringApplication.run(FantryApplication.class, args);
    }

}
