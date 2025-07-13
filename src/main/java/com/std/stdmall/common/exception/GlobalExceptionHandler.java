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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        log.error("HttpMediaTypeNotSupportedException Occurred: {}", ex.getMessage(), ex);

        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        String detailMessage = String.format("지원되지 않는 미디어 타입입니다. 요청된 Content-Type: %s, 지원되는 타입: %s",
                ex.getContentType(), supportedMediaTypes);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name())
                .message(detailMessage)
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .build();

        // HttpStatus.UNSUPPORTED_MEDIA_TYPE (415) 반환
        return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        log.error("HttpMessageNotReadableException Occurred: {}", ex.getMessage(), ex);

        // 상세 에러 메시지 추출 (개발 단계에서는 유용할 수 있으나, 프로덕션에서는 일반적인 메시지 권장)
        String detailMessage = "유효하지 않은 요청 본문입니다. 올바른 JSON 형식을 확인해주세요.";
        if (ex.getMessage() != null && ex.getMessage().contains("JSON parse error")) {
            // JSON 파싱 오류의 경우 좀 더 구체적인 메시지를 추출하여 제공
            detailMessage += " JSON 파싱 오류: " + ex.getMessage().split("JSON parse error: ")[1].split("\n at")[0];
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(HttpStatus.BAD_REQUEST.name()) // 또는 적절한 커스텀 에러 코드 (예: "C006")
                .message(detailMessage)
                .timestamp(LocalDateTime.now())
                .path(((ServletWebRequest)request).getRequest().getRequestURI())
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        // HttpStatus.BAD_REQUEST (400) 반환
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
