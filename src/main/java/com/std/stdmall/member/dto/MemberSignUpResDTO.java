package com.std.stdmall.member.dto;

import com.std.stdmall.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSignUpResDTO {
    private Long memberNum;
}
