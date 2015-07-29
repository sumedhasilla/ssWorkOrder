package com.ss.workOrder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {

    public NoContentException(final String message) {
        super(message);
    }

}
