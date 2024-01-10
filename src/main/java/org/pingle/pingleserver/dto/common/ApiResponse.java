package org.pingle.pingleserver.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.dto.type.SuccessMessage;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T>{

    private final int code;
    private final String message;
    private T data;

    public static <T> ApiResponse<T> success(SuccessMessage success) {
        return new ApiResponse<>(success.getHttpStatusCode(), success.getMessage());
    }

    public static <T> ApiResponse<T> success(SuccessMessage success, T data) {
        return new ApiResponse<>(success.getHttpStatusCode(), success.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(ErrorMessage fail) {
        return new ApiResponse<>(fail.getHttpStatusCode(), fail.getMessage());
    }
}