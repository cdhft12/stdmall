package com.std.stdmall.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//OncePerRequestFilter - > 모든 http 요청에 대하여 한번만 호출함
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출하는 헬퍼 메서드입니다.
     * "Authorization: Bearer <token>" 형태에서 "<token>" 부분만 추출합니다.
     * @param request 현재 HTTP 요청 객체
     * @return 추출된 JWT 토큰 문자열 (없으면 null)
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
// JWT 인증 실패로 인한 예외는 DispatcherServlet에 도달하기 전인 필터 체인 단계에서 ExceptionTranslationFilter에 의해 가로채지고 AuthenticationEntryPoint로 전달됩니다. 따라서 요청 자체가 컨트롤러로 넘어가지 않기 때문에, @ControllerAdvice는 이 예외들을 직접적으로 잡을 수 없습니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request); // Request Header에서 JWT 토큰 추출
        try {
            if (StringUtils.hasText(jwt)) {
                // JWT 토큰이 존재하면 유효성 검사를 시도합니다.
                jwtTokenProvider.validateToken(jwt); // void 메서드 호출
                // 예외가 발생하지 않았다면 (토큰이 유효하다면) 인증 정보 설정
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (org.springframework.security.core.AuthenticationException authException) {
            // jwtTokenProvider.validateToken()에서 던져진 CustomAuthException (AuthenticationException의 자식)을 잡습니다.
            // 이 예외를 다시 던져서 ExceptionTranslationFilter가 처리하도록 합니다.
            //logger.debug("인증 필터에서 AuthenticationException 발생: {}", authException.getMessage());
            throw authException; // ExceptionTranslationFilter로 예외 전달
        } catch (Exception e) {
            // CustomAuthException이 아닌 다른 예상치 못한 일반 예외 (예: RuntimeException 등) 처리
            logger.error("필터 내부에서 예상치 못한 오류 발생", e);
            throw new ServletException("필터 처리 중 알 수 없는 오류 발생", e);
        }

        // 인증 정보 설정이 완료되었거나, 토큰이 없어서 인증을 시도하지 않았거나,
        // 혹은 예외가 발생하여 위에서 이미 던져진 경우에만 다음 필터로 진행합니다.
        // 예외가 던져진 경우에는 이 라인까지 오지 않습니다.
        filterChain.doFilter(request, response);
    }
}
