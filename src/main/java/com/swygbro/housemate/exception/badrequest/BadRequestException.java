package com.swygbro.housemate.exception.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super(BadRequestType.DEFAULT.getMessage());
    }

    public BadRequestException(BadRequestType badRequestType) {
        super(badRequestType.getMessage());
    }

    public BadRequestException(String message) {
        super(message);
    }
}