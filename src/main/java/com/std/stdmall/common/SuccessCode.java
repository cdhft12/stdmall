package com.std.stdmall.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    SUCCESS(HttpStatus.OK,200,  "요청에 성공하였습니다.",true);

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
    private final Boolean isSuccess;


     SuccessCode(HttpStatus httpStatus, int code, String message, Boolean isSuccess) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
