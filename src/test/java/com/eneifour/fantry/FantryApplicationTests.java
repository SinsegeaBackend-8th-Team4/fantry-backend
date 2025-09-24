package com.eneifour.fantry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class FantryApplicationTests {
    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }
}
