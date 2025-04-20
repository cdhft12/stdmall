package com.std.stdmall.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ErrorCode {
    BAD_REQUEST( HttpStatus.BAD_REQUEST,400,  "입력값을 확인해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN,  403,"권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404,"대상을 찾을 수 없습니다."),
    INVALID_UID_FORMAT(HttpStatus.BAD_REQUEST,400,"아이디 정규 표현식 예외입니다."),
    INVALID_PW_FORMAT(HttpStatus.BAD_REQUEST,400,"비밀번호 정규 표현식 예외입니다."),
    INVALID_BIRTH_FORMAT(HttpStatus.BAD_REQUEST,400,"생년월일 정규 표현식 예외입니다."),
    ID_ALREADY_EXISTS(HttpStatus.CONFLICT,409,"중복된 아이디 입니다."),
    PASSWORD_ENCRYPTION_FAILURE(INTERNAL_SERVER_ERROR,500,"비밀번호 암호화에 실패했습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR,500,"데이터베이스 오류입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String errorMessage;

    ErrorCode( HttpStatus httpStatus, int code, String errorMessage) {

        this.httpStatus = httpStatus;
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
