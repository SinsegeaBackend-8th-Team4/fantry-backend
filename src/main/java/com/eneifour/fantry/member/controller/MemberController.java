package com.eneifour.fantry.member.controller;

import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.member.exception.EmailAlreadyExistsException;
import com.eneifour.fantry.member.model.JoinService;
import com.eneifour.fantry.member.model.RedisCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
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
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = redisCodeService.verifyCode6(email, code);
        if(isVerified) {
            return ResponseEntity.ok(Map.of("result", "인증 성공"));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", "인증 실패"));
        }
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
        try {
            joinService.join(memberDTO, roleType);
            return ResponseEntity.ok("가입 완료");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body((Map.of("error", e.getMessage())));
        }

    }

    //아이디 찾기
    @GetMapping("/user/findId")
    public ResponseEntity<?> findId(@RequestParam String email){
        MemberDTO memberDTO = joinService.findMemberByEmail(email);
        return ResponseEntity.ok(Map.of("result", "회원 찾기 완료", "member", memberDTO));
    }

}
