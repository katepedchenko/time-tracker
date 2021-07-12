package com.example.timetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFinishedActivityException extends RuntimeException {

    // todo
    public NotFinishedActivityException(UUID userId) {
        super(String.format("User %s has not finished activity. Please, finish last one and start new",
                userId));
    }
}
