package com.eneifour.fantry.member.controller;

import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.Role;
import com.eneifour.fantry.member.dto.MemberCreateRequest;
import com.eneifour.fantry.member.dto.MemberResponse;
import com.eneifour.fantry.member.dto.MemberUpdateRequest;
import com.eneifour.fantry.member.service.MemberService;
import com.eneifour.fantry.security.dto.TokenMemberResponse;
import com.eneifour.fantry.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    //access 토큰으로 멤버 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<?> getMember(){
        String username = SecurityUtil.getUserName();
        TokenMemberResponse response = memberService.findMemberResponseBy(username);
        return ResponseEntity.ok().body(Map.of("result", "회원 찾기 완료", "member", response));
    }

    //모든 회원 가져오기
    @GetMapping("/")
    public ResponseEntity<?> getMembers(){
        List<MemberResponse> memberList = memberService.getMembers();
        return ResponseEntity.ok().body(Map.of("memberList", memberList));
    }

    //한명의 회원 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable String id){
        MemberResponse member = memberService.getMemberById(id);
        return ResponseEntity.ok().body(Map.of("member", member));
    }

    //신규 회원 추가하기
    @PostMapping("/")
    public ResponseEntity<?> addMember(@RequestBody MemberCreateRequest memberCreateRequest){
        memberService.saveMember(memberCreateRequest);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 등록됨"));
    }

    //회원 수정하기
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable String id, @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(memberUpdateRequest);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 수정됨"));
    }

    //회원 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id){
        memberService.deleteMemberById(id);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 삭제됨"));
    }

    //회원 삭제하기(플래그 변경)
    @PutMapping("/{id}/delete")
    public ResponseEntity<?> deleteMemberById(@PathVariable String id){
        memberService.deactiveateMember(id);
        return ResponseEntity.ok().body(Map.of("result", "회원이 성공적으로 비활성화 됨"));
    }

    //회원의 권한을 수정
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateMemberRole(@PathVariable String id, @RequestBody Role role){
        memberService.updateMemberRole(id, role);
        return ResponseEntity.ok().body(Map.of("result", "회원의 권한이 성공적으로 수정되었습니다."));
    }
}