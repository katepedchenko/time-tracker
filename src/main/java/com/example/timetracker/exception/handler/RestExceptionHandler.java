package com.example.timetracker.exception.handler;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        ResponseStatus status = AnnotatedElementUtils
                .findMergedAnnotation(ex.getClass(), ResponseStatus.class);

        HttpStatus httpStatus = status != null ?
                status.code() :
                HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorInfo errorInfo = new ErrorInfo(httpStatus, ex.getClass(),
                ex.getMessage());
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), httpStatus);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().toString();

        ErrorInfo errorInfo = new ErrorInfo(httpStatus, ex.getClass(), errorMessage);
        return new ResponseEntity<>(errorInfo, new HttpHeaders(), httpStatus);
    }
}
