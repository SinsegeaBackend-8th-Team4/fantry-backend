package com.eneifour.fantry.mail.controller;

import com.eneifour.fantry.mail.MailRequest;
import com.eneifour.fantry.mail.model.MailService;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.member.model.RedisCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/send")
public class MailController {
    private final MailService mailService;
    private final RedisCodeService redisCodeService;

    /*----------------------------------------------
    *  기본 메일 요청
    *  사용: 매개변수로 (받는 사람 이메일주소, 메일제목, 내용, 링크(선택)) 입력
    *  입력 구조: form-data 방식
    * ---------------------------------------------*/
    @PostMapping("/mail")
    public String sendMailForm(@RequestParam String to, @RequestParam String subject, @RequestParam String text, @RequestParam String link,  @RequestParam String linkTitle) {
        boolean isSent = mailService.sendEmail(to, subject, text, link, linkTitle);
        return isSent ? "Mail sent successfully" : "Mail not sent";
    }
    /*----------------------------------------------
     *  사용: 매개변수로 (받는 사람 이메일주소, 메일제목, 내용, 링크(선택)) 입력
     *  입력 구조: json 방식
     * ---------------------------------------------*/
    @PostMapping("/mail/json")
    public String sendMailJsonForm(@RequestBody MailRequest mailRequest) {
        boolean isSent = mailService.sendEmail(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getText(),mailRequest.getLink(), mailRequest.getLinkTitle());
        return isSent ? "Mail sent successfully" : "Mail not sent";
    }

    //인증 코드 발송
    @PostMapping("authCode")
    public ResponseEntity<?> getCode(@RequestBody MemberDTO memberDTO) {
        String email = memberDTO.getEmail();
        String code = redisCodeService.savePending(memberDTO);
        long ttlSeconds = redisCodeService.getRemainingTTL(email);

        String subject = "Fantry 인증코드 발송";
        try {
            mailService.sendEmail(email, subject, code, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("result", "메일 전송 실패", "error", "메일 서버 오류나 네트워크 문제로 인해 메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.")
            );
        }
        return ResponseEntity.ok(Map.of(
                "result", "인증 코드 전송 완료",
                "ttl", ttlSeconds
        ));
    }

    // 회원가입 완료 시 보내지는 요청
    @PostMapping("/welcome")
    public String sendWelcome(@RequestParam String to) {
        String subject = "Fantry 회원가입을 환영합니다!";
        String content = "<p>회원 가입이 완료되었습니다. <br>여기에서 로그인하고 다양한 아이돌 굿즈를 거래해 보세요!</p>";
        boolean isSent = mailService.sendEmail(to, subject, content, null, null);
        return isSent ? "회원가입 메일이 성공적으로 발송되었습니다." : "메일 발송 실패";
    }

    //비밀번호 재설정 요청 -- 추후에 수정 필요
    @PostMapping("/reset-password")
    public String sendPasswordResetEmail(@RequestParam String to, @RequestParam String resetLink) {
        String subject = "비밀번호 재설정 요청";
        String content = "<p>비밀번호 재설정을 위해 아래 링크를 클릭하세요.</p>";
        boolean isSent = mailService.sendEmail(to, subject, content, resetLink, "여기를 눌러 비밀번호를 재설정하세요!");
        return isSent ? "비밀번호 재설정 메일이 성공적으로 발송되었습니다." : "메일 발송 실패";
    }
}
