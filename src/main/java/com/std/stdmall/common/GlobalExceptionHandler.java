package com.std.stdmall.common;


import com.std.stdmall.configuration.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
        ResponseEntityExceptionHandler 를 사용하도록 개발해야함 다시 수정할것
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException e) {
        String errorMessage = e.getErrorCode().getErrorMessage();
        int errorCode = e.getErrorCode().getCode();

        BaseResponse<?> response = new BaseResponse<>(errorMessage, errorCode);
        return ResponseEntity
                .status(errorCode)
                .body(response);
    }


}
