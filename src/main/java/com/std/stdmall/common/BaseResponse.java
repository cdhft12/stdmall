package com.std.stdmall.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.std.stdmall.common.SuccessCode.SUCCESS;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 성공
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.getIsSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.message = errorCode.getErrorMessage();
        this.code= errorCode.getCode();
    }

    public BaseResponse(String message, int code) {
        this.isSuccess = false;
        this.message = message;
        this.code = code;
    }
}
