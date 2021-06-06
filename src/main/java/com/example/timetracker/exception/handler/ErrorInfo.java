package com.example.timetracker.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorInfo {

    private final HttpStatus httpStatus;
    private final Class exceptionClass;
    private final String message;
}