package com.eneifour.fantry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FantryApplicationTests {
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }

}
