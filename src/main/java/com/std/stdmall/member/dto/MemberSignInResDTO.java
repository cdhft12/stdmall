package com.std.stdmall.member.dto;

import com.std.stdmall.common.MemberRole;
import lombok.*;

@Getter
@Setter
@Builder
public class MemberSignInResDTO {
    private String token;
}
