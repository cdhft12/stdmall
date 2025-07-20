package com.std.stdmall.common.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthException extends AuthenticationException {
    private final ResultCode resultCode;

    // ResultCode만으로 예외를 생성하는 생성자
    public CustomAuthException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    // ResultCode와 함께 특정 메시지를 추가로 전달해야 할 경우 (선택 사항)
    public CustomAuthException(ResultCode resultCode, String detailMessage) {
        super(detailMessage);
        this.resultCode = resultCode;
    }

    // 예외의 원인(cause)을 포함해야 할 경우의 생성자
    public CustomAuthException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    // 예외의 원인과 추가 메시지를 모두 포함해야 할 경우의 생성자
    public CustomAuthException(ResultCode resultCode, String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        this.resultCode = resultCode;
    }
}
