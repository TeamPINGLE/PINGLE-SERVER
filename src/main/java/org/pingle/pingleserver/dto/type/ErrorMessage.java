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
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    ALREADY_REGISTERED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    INVALID_GROUP_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 그룹 코드입니다."),
    INVALID_HEADER_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 헤더입니다."),
    INVALID_PROVIDER_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 소셜 플랫폼입니다."),
    MISSING_REQUIRED_HEADER(HttpStatus.BAD_REQUEST, "필수 헤더가 누락되었습니다."),
    // Authorization Error 401
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "토큰이 제공되지 않았거나 유효하지 않습니다."),
    EMPTY_PRINCIPAL(HttpStatus.UNAUTHORIZED, "내부적으로 유저 정보를 받는데 실패했습니다"),
    // Permission Denied 403
    GROUP_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 사용자는 그룹에 속해 있지 않습니다."),
    // Not Found Error 404
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    // Method Not Allowed Error 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다."),
    // Conflict Error 409
    RESOURCE_CONFLICT(HttpStatus.CONFLICT, "리소스 충돌이 일어났습니다."),
    // Internal Server Error 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus status;
    private final String message;

    public int getHttpStatusCode() {
        return this.status.value();
    }
}
