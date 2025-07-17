package com.std.stdmall.web;

import com.std.stdmall.common.exception.CustomException;
import com.std.stdmall.common.exception.ResultCode;
import com.std.stdmall.common.jwt.JwtTokenProvider;
import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 및 검증
    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 생성용

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    //회원 가입
    @Transactional
    public MemberSignUpResDTO signUpMember(MemberSignUpReqDTO reqDTO)  {
        if (memberRepository.existsByLoginId(reqDTO.getLoginId())) {
            throw new CustomException(ResultCode.DUPLICATE_USERNAME);
        }
        reqDTO.setPassword(passwordEncoder.encode(reqDTO.getPassword()));
        Member member = memberRepository.save(reqDTO.toEntity());
        MemberSignUpResDTO memberSignUpResDTO = MemberSignUpResDTO.builder()
                .memberNum(member.getMemberNum())
                .build();
        return memberSignUpResDTO;
    }

    /**
     * 로그인 요청을 처리하고, 성공 시 JWT 토큰을 발급합니다.
     */
    @Transactional
    public String generateTokenAfterAuthentication(Authentication authentication) {
        // SecurityContextHolder에 인증 정보 저장 (컨트롤러에서 인증 후 호출)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT 토큰 생성
        return jwtTokenProvider.createToken(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));
    }
}
