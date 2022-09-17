package com.swygbro.housemate.group.service;

import com.swygbro.housemate.group.validator.ValidatorURI;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LinkCreator {

    public String executor(String userId, List<ValidatorURI> validator) {
        String uri = userId + "-" + RandomStringUtils.randomNumeric(4) + "-" + System.currentTimeMillis();
        validator.stream()
                .filter(v -> v.valid(uri))
                .map(v -> new IllegalStateException("Validation Fail"));

        return uri;
    }

}
