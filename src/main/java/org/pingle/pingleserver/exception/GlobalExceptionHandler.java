package org.pingle.pingleserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<?>> handleNoPageFoundException(Exception e) {
        log.error("handleNoPageFoundException() in GlobalExceptionHandler throw NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.fail(ErrorMessage.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
        log.error("handleException() in GlobalExceptionHandler throw BusinessException : {}", e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(ApiResponse.fail(e.getErrorMessage()));
    }
//    @ExceptionHandler(RestClientResponseException.class)
//    public ResponseEntity<ApiResponse<?>> handleRestClientResponseException(RestClientResponseException e) {
//        if (e.getStatusCode().value() == 400) {
//            return ResponseEntity.status(e.getStatusCode().value()).body(ApiResponse.fail(ErrorMessage.INVALID_PARAMETER_ERROR));
//
//        }
//
//        return ResponseEntity.status(e.getStatusCode().value()).body(ApiResponse.fail(ErrorMessage.NAVER_SERVER_ERROR));
//    }
}
