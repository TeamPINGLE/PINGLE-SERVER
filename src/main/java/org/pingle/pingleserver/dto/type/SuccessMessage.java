package org.pingle.pingleserver.dto.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    OK(HttpStatus.OK, "OK"),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공");

    private final HttpStatus status;
    private final String message;

    public int getHttpStatusCode() {
        return this.status.value();
    }
}
