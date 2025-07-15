package com.std.stdmall.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse <T>{
    private final int status; // HTTP 상태 코드 (int 값)
    private final String code; // 애플리케이션 고유 코드
    private final String message; // 응답 메시지
    private final T data;

    // 일반 성공 (200 OK)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(ResultCode.OK.getHttpStatus().value())
                .code(ResultCode.OK.getCode())
                .message(ResultCode.OK.getMessage())
                .data(data)
                .build();
    }

    // 일반 성공 (200 OK, 데이터 없음)
    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .status(ResultCode.OK.getHttpStatus().value())
                .code(ResultCode.OK.getCode())
                .message(ResultCode.OK.getMessage())
                .build(); // data는 null이므로 설정하지 않음
    }

    // 특정 성공 코드 사용 (데이터 포함)
    public static <T> ApiResponse<T> success(ResultCode resultCode, T data) {
        // 성공 관련 ResultCode인지 확인하는 로직 추가 가능 (예: resultCode.getHttpStatus().is2xxSuccessful())
        return ApiResponse.<T>builder()
                .status(resultCode.getHttpStatus().value())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build();
    }
    // 특정 성공 코드 사용 (데이터 없음)
    public static ApiResponse<Void> success(ResultCode resultCode) {
        // 성공 관련 ResultCode인지 확인하는 로직 추가 가능
        return ApiResponse.<Void>builder()
                .status(resultCode.getHttpStatus().value())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .build();
    }
}
