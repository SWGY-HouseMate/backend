package com.swygbro.housemate.exception.badrequest;

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