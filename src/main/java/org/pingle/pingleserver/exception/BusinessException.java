package org.pingle.pingleserver.exception;

import lombok.Getter;
import org.pingle.pingleserver.dto.type.ErrorMessage;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public int getHttpStatusCode() {
        return errorMessage.getStatus().value();
    }
}
