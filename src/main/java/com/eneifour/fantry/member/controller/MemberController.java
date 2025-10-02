package com.eneifour.fantry.member.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.domain.RoleType;
import com.eneifour.fantry.member.dto.AuthCodeDTO;
import com.eneifour.fantry.member.dto.MemberDTO;
import com.eneifour.fantry.member.model.JoinService;
import com.eneifour.fantry.member.model.MemberService;
import com.eneifour.fantry.member.model.RedisCodeService;
import com.eneifour.fantry.security.dto.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final JoinService joinService;
    private final RedisCodeService redisCodeService;
    private final MemberService memberService;

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

    //access 토큰으로 멤버 정보 가져오기
    @GetMapping("/member/me")
    public ResponseEntity<?> getMember(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberResponse response = memberService.findMemberResponseBy(username);
        return ResponseEntity.ok().body(Map.of("result", "회원 찾기 완료", "member", response));
    }

    //모든 회원 가져오기
    @GetMapping("/member")
    public ResponseEntity<?> getMembers(){
        List<Member> memberList = memberService.getMembers();
        return ResponseEntity.ok().body(Map.of("memberList", memberList));
    }

    //한명의 회원 가져오기
    @GetMapping("/member/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable String id){
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok().body(Map.of("member", member));
    }

    //신규 회원 추가하기
    @PostMapping("/member")
    public ResponseEntity<?> addMember(@RequestBody Member member){
        memberService.saveMember(member);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 등록됨"));
    }

    //회원 수정하기
    @PutMapping("/member/{id}")
    public ResponseEntity<?> updateMember(@PathVariable String id, @RequestBody Member member){
        memberService.updateMember(member);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 수정됨"));
    }

    //회원 삭제하기
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id){
        memberService.deleteMemberById(id);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 삭제됨"));
    }

    //회원의 권한을 수정
    @PutMapping("/member/{id}/role")
    public ResponseEntity<?> updateMemberRole(@PathVariable String id, @RequestBody Role role){
        memberService.updateMemberRole(id, role);
        return ResponseEntity.ok().body(Map.of("result", "회원의 권한이 성공적으로 수정되었습니다."));
    }
}