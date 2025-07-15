package com.std.stdmall.member.dto;

import com.std.stdmall.common.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignInResDTO {
    private String loginId;
    private String password;
    private String name;
    private MemberRole role;
}
