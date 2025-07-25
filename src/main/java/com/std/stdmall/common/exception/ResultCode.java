package com.std.stdmall.common.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // 성공
    OK(HttpStatus.OK, "S001", "성공"),
    CREATED(HttpStatus.CREATED, "S002", "리소스 생성 성공"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "S003", "콘텐츠 없음"),
    // Common (공통 에러)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E001", "유효하지 않은 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E002", "허용되지 않는 HTTP 메서드입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "E003", "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E004", "서버 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E005", "유효하지 않은 URL 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E006", "인증 실패: 잘못된 사용자 이름 또는 비밀번호입니다."),
    // Member (사용자 관련 에러) - U로 시작하는 코드
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "M002", "이미 존재하는 사용자 이름입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M003", "비밀번호가 일치하지 않습니다."),
    //jwt 예외
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "J001", "만료된 JWT 토큰입니다. 다시 로그인 해주세요."),
    JWT_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "J002", "유효하지 않은 JWT 서명입니다."),
    JWT_UNSUPPORTED(HttpStatus.BAD_REQUEST, "J003", "지원되지 않는 JWT 토큰 형식입니다."),
    JWT_MALFORMED(HttpStatus.BAD_REQUEST, "J004", "잘못된 형식의 JWT 토큰입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
