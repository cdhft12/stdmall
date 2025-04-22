package com.std.stdmall.member.service;

import com.std.stdmall.common.ErrorCode;
import com.std.stdmall.configuration.exception.BaseException;
import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.std.stdmall.common.ErrorCode.BAD_REQUEST;


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
    public MemberSignUpResDTO signUpMember(MemberSignUpReqDTO reqDTO) throws BaseException {
        if(checkIsEmpty(reqDTO)){
            throw new BaseException(BAD_REQUEST);
        }
        reqDTO.setPassword(bCryptPasswordEncoder.encode(reqDTO.getPassword()));
        Member member = memberRepository.save(reqDTO.toEntity());
        MemberSignUpResDTO memberSignUpResDTO = MemberSignUpResDTO.builder()
                .memberNum(member.getMemberNum())
                .build();
        return memberSignUpResDTO;
    }
    public boolean checkIsEmpty(MemberSignUpReqDTO memberSignUpReqDTO){
        return memberSignUpReqDTO.getLoginId().length()==0 || memberSignUpReqDTO.getPassword().length()==0 ;
    }


}
