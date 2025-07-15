package com.std.stdmall.member.dto;

import com.std.stdmall.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignInReqDTO {
    private String loginId;
    private String password;
    private String name;

    public Member toEntity() {
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
    }
}
