package com.mexpedition.mexpedition.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExpeditionException extends RuntimeException {

    public ExpeditionException(String message) {
        super(message);
    }
}
