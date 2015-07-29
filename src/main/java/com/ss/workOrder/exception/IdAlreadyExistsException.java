package com.ss.workOrder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.CONFLICT)
public class IdAlreadyExistsException extends RuntimeException {

    public IdAlreadyExistsException(final String message) {
        super(message);
    }

}
