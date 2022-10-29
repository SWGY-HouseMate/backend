package com.swygbro.housemate.exception.forbidden;

public class CAuthenticationEntryPointException extends RuntimeException {
    public CAuthenticationEntryPointException() {
        super(ForbiddenType.DEFAULT.getMessage());
    }

    public CAuthenticationEntryPointException(String message) {
        super(message);
    }

    public CAuthenticationEntryPointException(ForbiddenType dataType) {
        super(dataType.getMessage());
    }
}