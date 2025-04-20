package com.std.stdmall.member.dto;

import com.std.stdmall.common.Gender;
import com.std.stdmall.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpReqDTO {
    private String loginId;
    private String password;
    private String name;
    private Gender gender;
    private LocalDate birthday;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .gender(gender)
                .birthday(birthday)
                .deleteYn(false)
                .build();
    }
}
