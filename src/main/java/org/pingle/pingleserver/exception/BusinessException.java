package org.pingle.pingleserver.exception;

import lombok.Getter;
import org.pingle.pingleserver.dto.type.ErrorMessage;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorMessage message;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        message = errorMessage;
    }

    public int getHttpStatusCode() {
        return message.getStatus().value();
    }
}
