package com.eneifour.fantry.mail.model;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    //이메일 보내기
    public boolean sendEmail(String to, String subject, String content, String link, String linkTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String emailContent = buildEmailTemplate(subject, content, link, linkTitle);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            mailSender.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    //메일 내용 템플릿
    private String buildEmailTemplate(String subject, String content, String link, String linkTitle) {
        String emailBody = "<!DOCTYPE html>" +
                "<html lang=\"ko\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + subject + "</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333;\">" +
                "<div style=\"width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px;\">" +
                "<div style=\"background-color: #6a1b9a; padding: 20px; color: #fff; text-align: center; border-radius: 8px 8px 0 0;\">" +
                "<h1 style=\"margin: 0; font-size: 24px;\">Fantry</h1>" +
                "<p style=\"margin: 0; font-size: 14px;\">아이돌 굿즈 중고 거래/경매 사이트</p>" +
                "</div>" +
                "<div style=\"margin-top: 20px; font-size: 16px; line-height: 1.6;\">" +
                "<h2 style=\"font-size: 20px;\">" + subject + "</h2>" +
                "<p>" + content + "</p>";

        if (link != null && !link.isEmpty()) {
            emailBody += "<a href=\"" + link + "\" class=\"btn\">"+linkTitle+"</a>";
        }

        emailBody += "</div>" +
                "<div style=\"background-color: #eeeeee; padding: 10px; text-align: center; font-size: 12px; color: #777; border-radius: 0 0 8px 8px;\">" +
                "<p style=\"margin: 0;\">Fantry 고객센터: support@fantry.com</p>" +
                "<p style=\"margin: 0;\">Fantry © 2025, All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        return emailBody;
    }
}