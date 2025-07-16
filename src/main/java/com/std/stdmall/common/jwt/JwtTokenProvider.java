package com.std.stdmall.common.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;
@Component
@Slf4j
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final Key key;
    private final long validityInMilliseconds;

    //jwt.token-validity-in-seconds 토큰 만료 시간 초단위 정의
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token-validity-in-seconds}") long validityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secret); //Base64로 인코딩된 비밀 키 문자열을 바이트 배열로 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // 디코딩된 바이트 배열을 사용하여 HMAC SHA 키를 생성, 이 Key 객체는 JWT 서명(Signature)에 직접 사용
        this.validityInMilliseconds = validityInSeconds * 1000;
    }

    // JWT 토큰 생성
    /*
        Authentication  -  현재 인증된 사용자의 정보(사용자 이름, 권한 등)를 담고있는 객체
        authorities - Authentication 객체에서 사용자에게 부여된 권한(authorities) 목록을 추출후 쉼표로 구분된 단일 문자열로 변환
                      JWT의 auth 클레임에 저장되어 토큰을 파싱할 때 다시 권한으로 변환
        validity - 현재 시간에 미리 설정된 토큰 유효 기간(밀리초)을 더하여 토큰만료시간 계산
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName()) //사용자 이름
                .claim("auth", authorities) // 권한 정보 저장
                .setExpiration(validity) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS512) // 서명
                .compact();
    }
    // JWT 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)//jws 서명 검증
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
}
