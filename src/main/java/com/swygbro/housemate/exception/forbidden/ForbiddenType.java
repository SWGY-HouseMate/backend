package com.swygbro.housemate.exception.forbidden;

public enum ForbiddenType {

    DEFAULT("토큰이 유효하지 않습니다.")
    ;

    private final String message;

    ForbiddenType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}