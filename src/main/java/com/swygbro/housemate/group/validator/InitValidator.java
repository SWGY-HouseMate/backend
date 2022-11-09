package com.swygbro.housemate.group.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitValidator {
    static List<ValidatorURI> validatorURIList = new ArrayList<>();

    private final URIDuplicateValidator uriDuplicateValidator;

    @PostConstruct
    public void setValidator() {
        validatorURIList.add(uriDuplicateValidator);
    }

    public List<ValidatorURI> get() {
        return validatorURIList;
    }
}
