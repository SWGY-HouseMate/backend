package com.swygbro.housemate.group.service;

import com.swygbro.housemate.group.validator.InitValidator;
import com.swygbro.housemate.group.validator.ValidatorURI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkCreator {
    private final InitValidator initValidator;

    public String executor(String userId) {
        String uri = userId.substring(0, 3) + RandomStringUtils.randomNumeric(2) + "-" + System.currentTimeMillis();

        List<ValidatorURI> validatorURIList = initValidator.get();
        validatorURIList.stream()
                .filter(v -> v.valid(uri))
                .map(v -> new IllegalStateException("Validation Fail"));

        return uri;
    }
}
