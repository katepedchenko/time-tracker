package com.example.timetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ActionProhibitedException extends RuntimeException {

    public ActionProhibitedException(UUID activityId) {
        super(String.format("Action prohibited! Activity %s has already posted", activityId));
    }
}
