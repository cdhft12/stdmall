package com.std.stdmall.web;

import com.std.stdmall.common.exception.CustomException;
import com.std.stdmall.common.exception.ResultCode;
import com.std.stdmall.common.jwt.JwtTokenProvider;
import com.std.stdmall.member.domain.Member;
import com.std.stdmall.member.dto.MemberSignInReqDTO;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.member.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        // 2. 비즈니스 로직 검증 (예: 사용자 이름 중복 확인)
        if (memberRepository.existsByLoginId(reqDTO.getLoginId())) {
            //log.warn("이미 존재하는 사용자 이름입니다: {}", request.getUsername());
            throw new CustomException(ResultCode.DUPLICATE_USERNAME);
        }
        Member member = Member.builder()
                .loginId(reqDTO.getLoginId())
                .password(passwordEncoder.encode(reqDTO.getPassword())) // 비밀번호 암호화
                .name(reqDTO.getName()) // 이름, 성별, 생년월일 등은 회원가입 DTO에 포함되어야 함
                .gender(reqDTO.getGender()) // 예시값
                .birth(reqDTO.getBirth()) // 예시값
                .deleteYn(false) // 기본적으로 삭제 안됨
                .role(reqDTO.getRole()) // 기본 역할 부여
                .build();
        memberRepository.save(reqDTO.toEntity());

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
