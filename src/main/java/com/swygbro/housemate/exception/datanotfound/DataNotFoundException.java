package com.swygbro.housemate.exception.datanotfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
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