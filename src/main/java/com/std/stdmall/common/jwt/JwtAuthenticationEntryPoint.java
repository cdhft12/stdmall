package com.std.stdmall.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.std.stdmall.common.exception.CustomAuthException;
import com.std.stdmall.common.exception.ErrorResponse;
import com.std.stdmall.common.exception.ResultCode;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("인증 실패: {}", authException.getMessage(), authException);

        // 기본 응답 값 설정
        // ResultCode.UNAUTHORIZED를 기본으로 사용합니다.
        ResultCode baseResultCode = ResultCode.UNAUTHORIZED;
        HttpStatus httpStatus = baseResultCode.getHttpStatus();
        String errorMessage = baseResultCode.getMessage();
        String errorCode = baseResultCode.getCode();
        log.info( authException.getClass().getName());
        // 발생한 예외가 CustomAuthException인지 확인하여 상세 정보 활용
        if (authException instanceof CustomAuthException customAuthException) {
            ResultCode customResultCode = customAuthException.getResultCode();
            httpStatus = customResultCode.getHttpStatus(); // ResultCode에서 HTTP 상태 코드 가져오기
            errorMessage = customResultCode.getMessage(); // ResultCode에서 메시지 가져오기
            errorCode = customResultCode.getCode(); // ResultCode에서 코드 가져오기
        }
        // 그 외 다른 AuthenticationException 유형 처리 (예: 스프링 시큐리티 기본 예외)
        else {
            // 예를 들어, 스프링 시큐리티의 BadCredentialsException 같은 경우
            // 특정 에러 메시지에 따라 ResultCode를 매핑할 수도 있습니다.
            if (authException.getMessage().contains("Bad credentials")) {
                errorMessage = ResultCode.UNAUTHORIZED.getMessage(); // ResultCode의 UNAUTHORIZED 메시지 사용
                errorCode = ResultCode.UNAUTHORIZED.getCode();
                httpStatus = ResultCode.UNAUTHORIZED.getHttpStatus();
            }
            // 다른 AuthenticationException 유형에 대한 처리 추가 해야함
        }

        String originalPath = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (originalPath == null) {
            // 포워딩이 아닌 직접적인 요청이거나, 포워딩 속성이 없는 경우 현재 URI 사용
            originalPath = request.getRequestURI();
        }

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorMessage)
                .httpStatus(httpStatus) // ResultCode에서 얻은 HttpStatus 사용
                .timestamp(LocalDateTime.now())
                .path(originalPath)
                .errors(null)
                .build();
        // 응답 설정
        response.setStatus(httpStatus.value()); // ErrorResponse의 httpStatus 값으로 응답 상태 코드 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // JSON 응답 본문 작성
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
