package com.std.stdmall.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
    // 특정 메시지를 추가로 전달해야 할 경우
    public CustomException(ResultCode resultCode, String detailMessage) {
        super(detailMessage);
        this.resultCode = resultCode;
    }
}
