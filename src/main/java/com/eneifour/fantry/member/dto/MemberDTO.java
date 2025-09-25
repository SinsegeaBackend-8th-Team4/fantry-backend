package com.eneifour.fantry.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "아이디는 6~20자의 영문, 숫자만 가능합니다.")
    private String username;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자 포함 8자 이상이어야 합니다."
    )
    private String password;

    private String name;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "유효한 이메일 주소를 입력해주세요."
    )
    private String email;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Pattern(
            regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
            message = "휴대폰 번호는 01로 시작하며 '-' 없이 10~11자리여야 합니다."
    )
    private String phone;

    private String mailCode;
}
