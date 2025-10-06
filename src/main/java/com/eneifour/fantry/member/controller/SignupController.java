package com.eneifour.fantry.member.controller;

import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.member.dto.AuthCodeDTO;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.member.service.JoinService;
import com.eneifour.fantry.member.service.RedisCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignupController {
    private final JoinService joinService;
    private final RedisCodeService redisCodeService;

    //아이디 중복 체크
    @GetMapping("/user/checkId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String id) {
        boolean exists = joinService.isIdDuplicated(id);
        return ResponseEntity.ok().body(exists);
    }

    //인증번호 검증
    @PostMapping("/user/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody AuthCodeDTO authCodeDTO){
        redisCodeService.verifyCode6(authCodeDTO);
        return ResponseEntity.ok().body(Map.of("result", "인증 성공"));
    }

    //회원가입
    @PostMapping("/{type}/signup")
    public ResponseEntity<?> signup(@PathVariable("type")String type, @Valid @RequestBody MemberDTO memberDTO, BindingResult bindingResult){
        RoleType roleType;
        switch (type.toLowerCase()){
            case "user":
                roleType = RoleType.USER;
                break;
            case "admin":
                roleType = RoleType.ADMIN;
                break;
            default:
                return ResponseEntity.badRequest().body("잘못된 회원 유형입니다.");
        }
        //유효성 체크
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        joinService.join(memberDTO, roleType);
        return ResponseEntity.ok().body(Map.of("result","가입 완료"));

    }

    //아이디 찾기
    @GetMapping("/user/findId")
    public ResponseEntity<?> findId(@RequestParam String email){
        MemberDTO memberDTO = joinService.findMemberByEmail(email);
        return ResponseEntity.ok().body(Map.of("result", "회원 찾기 완료", "member", memberDTO));
    }
}
