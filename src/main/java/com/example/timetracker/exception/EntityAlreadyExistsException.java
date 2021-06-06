package com.example.timetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(Class entityClass, String externalId) {
        super(String.format("Entity %s with externalId=%s already exists",
                entityClass, externalId));
    }
}
