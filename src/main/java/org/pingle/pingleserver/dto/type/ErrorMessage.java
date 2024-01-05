package org.pingle.pingleserver.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    // Apple Login Error
    INVALID_APPLE_PUBLIC_KEY(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple Public Key입니다."),
    INVALID_APPLE_IDENTITY_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 Apple Identity Token입니다."),
    EXPIRED_APPLE_IDENTITY_TOKEN(HttpStatus.BAD_REQUEST, "만료된 Apple Identity Token입니다."),
    CREATE_PUBLIC_KEY_EXCEPTION(HttpStatus.BAD_REQUEST, "Apple Public verify에 실패했습니다."),
    // JWT Error
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    JWT_TOKEN_IS_EMPTY(HttpStatus.UNAUTHORIZED, "JWT 토큰이 비어있습니다."),
    // Invalid Argument Error 400
    INVALID_HEADER_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 헤더입니다."),
    INVALID_PROVIDER_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 소셜 플랫폼입니다."),
    // Authorization Error 401
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    LOGIN_FAIL_ERROR(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    NO_SUCH_USER(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다."),
    // Not Found Error 404
    USER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    // Method Not Allowed Error 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    // Internal Server Error 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String message;

    public int getHttpStatusCode() {
        return this.status.value();
    }
}
