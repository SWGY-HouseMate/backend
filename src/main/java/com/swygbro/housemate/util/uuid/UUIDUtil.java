package com.swygbro.housemate.util.uuid;

import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class UUIDUtil {

    public String create() {
        return randomUUID().toString();
    }

}
