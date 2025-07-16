package com.std.stdmall.member.dto;

import com.std.stdmall.common.Gender;
import com.std.stdmall.common.MemberRole;
import com.std.stdmall.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpReqDTO {
    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
    @Size(min = 5, max = 20, message = "사용자 이름은 5자 이상 20자 이하로 입력해주세요.")
    private String loginId;
    @NotBlank(message = "패스워드는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    private String password;
    private String name;
    private Gender gender;
    private LocalDate birth;
    private MemberRole role;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .gender(gender)
                .birth(birth)
                .deleteYn(false)
                .role(role)
                .build();
    }
}
