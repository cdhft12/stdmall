package com.std.stdmall.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private final String errorCode;
    private final String message;
    private HttpStatus httpStatus;
    private final LocalDateTime timestamp;
    private final String path;
    private final List<FieldError> errors;
    @Getter
    @Builder
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}
