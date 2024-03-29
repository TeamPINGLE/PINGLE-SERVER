package org.pingle.pingleserver.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.dto.common.ApiResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.utils.SlackUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackUtil slackUtil;

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> handlerMethodArgumentNotValidException(Exception e) {
        log.error("handlerMethodArgumentNotValidException() in GlobalExceptionHandler throw MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.BAD_REQUEST));
    }

    @ExceptionHandler(value={MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<?>> handlerMethodArgumentTypeMismatchException(Exception e) {
        log.error("handlerMethodArgumentTypeMismatchException() in GlobalExceptionHandler throw MethodArgumentTypeMismatchException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.BAD_REQUEST));
    }

    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    public ResponseEntity<ApiResponse<?>> handlerHandlerMethodValidationException(Exception e) {
        log.error("handlerHandlerMethodValidationException() in GlobalExceptionHandler throw HandlerMethodValidationException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.BAD_REQUEST));
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class})
    public ResponseEntity<ApiResponse<?>> handlerMissingRequestHeaderException(Exception e) {
        log.error("handlerMissingRequestHeaderException() in GlobalExceptionHandler throw MissingRequestHeaderException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.MISSING_REQUIRED_HEADER));
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<ApiResponse<?>> handlerMissingServletRequestParameterException(Exception e) {
        log.error("handlerMissingServletRequestParameterException() in GlobalExceptionHandler throw MissingServletRequestParameterException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.MISSING_REQUIRED_PARAMETER));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("handleMessageNotReadableException() in GlobalExceptionHandler throw HttpMessageNotReadableException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ErrorMessage.BAD_REQUEST));
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNoPageFoundException(Exception e) {
        log.error("handleNoPageFoundException() in GlobalExceptionHandler throw NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(ErrorMessage.NOT_FOUND_END_POINT));
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<?>> handleMethodNotSupportedException(Exception e) {
        log.error("handleMethodNotSupportedException() in GlobalExceptionHandler throw HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.fail(ErrorMessage.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        log.error("handleException() in GlobalExceptionHandler throw BusinessException : {}", e.getErrorMessage());
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(ApiResponse.fail(e.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handlerException(Exception e) {
        log.error("handlerException() in GlobalExceptionHandler throw Exception : {} {}", e.getClass(), e.getMessage());
        slackUtil.alertError(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(ErrorMessage.INTERNAL_SERVER_ERROR));
    }
}
