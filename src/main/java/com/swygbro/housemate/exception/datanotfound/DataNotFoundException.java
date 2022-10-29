package com.swygbro.housemate.exception.datanotfound;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super(DataNotFoundType.DEFAULT.getMessage());
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(DataNotFoundType dataType) {
        super(dataType.getMessage());
    }

}