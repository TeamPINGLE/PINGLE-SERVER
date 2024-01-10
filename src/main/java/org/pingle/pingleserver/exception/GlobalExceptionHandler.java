package org.pingle.pingleserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<?>> handleNoPageFoundException(Exception e) {
        log.error("handleNoPageFoundException() in GlobalExceptionHandler throw NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(ErrorMessage.NOT_FOUND_END_POINT));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> handlerMethodArgumentNotValidException(Exception e) {
        log.error("handlerMethodArgumentNotValidException() in GlobalExceptionHandler throw MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.BAD_REQUEST));
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<ApiResponse<?>> handlerMissingRequestHeaderException(Exception e) {
        log.error("handlerMissingRequestHeaderException() in GlobalExceptionHandler throw MissingRequestHeaderException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.MISSING_REQUIRED_HEADER));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(CustomException e) {
        log.error("handleException() in GlobalExceptionHandler throw BusinessException : {}", e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(ApiResponse.fail(e.getErrorMessage()));
    }
}
