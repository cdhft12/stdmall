package com.std.stdmall.member.service;

import com.std.stdmall.common.MemberRole;
import com.std.stdmall.common.exception.CustomException;
import com.std.stdmall.common.exception.ErrorCode;
import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional(readOnly = true)
    public List<Member> memberList() {
        return (List<Member>)memberRepository.findAll();
    }
    @Override
    public MemberSignUpResDTO signUpMember(MemberSignUpReqDTO reqDTO)  {
        // 2. 비즈니스 로직 검증 (예: 사용자 이름 중복 확인)
        if (memberRepository.existsByLoginId(reqDTO.getLoginId())) {
            //log.warn("이미 존재하는 사용자 이름입니다: {}", request.getUsername());
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
        reqDTO.setPassword(bCryptPasswordEncoder.encode(reqDTO.getPassword()));
        reqDTO.setRole(MemberRole.USER);
        Member member = memberRepository.save(reqDTO.toEntity());
        MemberSignUpResDTO memberSignUpResDTO = MemberSignUpResDTO.builder()
                .memberNum(member.getMemberNum())
                .build();
        return memberSignUpResDTO;
    }


}
