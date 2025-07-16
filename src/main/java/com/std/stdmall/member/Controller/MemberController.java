package com.std.stdmall.member.Controller;

import com.std.stdmall.common.exception.ApiResponse;
import com.std.stdmall.common.exception.ResultCode;
import com.std.stdmall.member.dto.MemberSignInReqDTO;
import com.std.stdmall.member.dto.MemberSignInResDTO;
import com.std.stdmall.member.dto.MemberSignUpReqDTO;
import com.std.stdmall.member.dto.MemberSignUpResDTO;
import com.std.stdmall.web.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity signUp(@Validated MemberSignUpReqDTO reqDTO) {
        MemberSignUpResDTO memberSignUpRes = memberService.signUpMember(reqDTO);
        ApiResponse<MemberSignUpResDTO> apiResponse = ApiResponse.success(ResultCode.CREATED, memberSignUpRes);
        return new ResponseEntity<>(apiResponse, ResultCode.CREATED.getHttpStatus());
    }
    @PostMapping("/login")
    public ResponseEntity<MemberSignInResDTO> login(@Validated MemberSignInReqDTO memberSignInReqDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(memberSignInReqDTO.getLoginId(), memberSignInReqDTO.getPassword())
            );
            // 인증 성공 후, MemberService에게 JWT 토큰 생성을 위임
            String jwtToken = memberService.generateTokenAfterAuthentication(authentication);
            return ResponseEntity.ok(MemberSignInResDTO.builder().token(jwtToken).build());
        } catch (AuthenticationException e) { // Spring Security 인증 관련 예외를 명확히 처리
            // BadCredentialsException, UsernameNotFoundException 등 포함
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // 기타 예상치 못한 예외
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
