package com.example.timetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFinishedWorkdayException extends RuntimeException {

    public NotFinishedWorkdayException(UUID userId) {
        super(String.format("User %s has not finished workday entries. Please, finish last one and start new",
                userId));
    }
}
