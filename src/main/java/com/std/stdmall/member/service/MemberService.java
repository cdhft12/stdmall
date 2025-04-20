package com.std.stdmall.member.service;

import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {
    List<Member> memberList();
    MemberSignUpResDTO signUpMember(MemberSignUpReqDTO reqDTO);
}
