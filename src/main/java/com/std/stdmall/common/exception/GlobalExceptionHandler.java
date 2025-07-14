package com.std.stdmall.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .httpStatus(ex.getErrorCode().getHttpStatus())
                .build();
        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
    /*WebRequest
    * 사용 이유 = 환경 독립성, 공통화된 접근을 위해서
    * */
    // @Valid 또는 @Validated 유효성 검사 실패 시 발생하는 MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .value(Objects.toString(error.getRejectedValue(), "")) // null 방지
                        .reason(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_INPUT_VALUE.getCode()) // Enum 활용
                .message(ErrorCode.INVALID_INPUT_VALUE.getMessage())
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .errors(fieldErrors) // 필드별 에러 목록 추가
                .httpStatus(ErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
     // 그 외 모든 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.") // 사용자에게 일반적인 메시지 제공
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
