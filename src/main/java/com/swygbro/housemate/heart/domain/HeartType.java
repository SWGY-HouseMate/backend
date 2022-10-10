package com.swygbro.housemate.heart.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum HeartType implements EnumModel {

    고마워요("THANKS"),
    미안해요("SORRY"),
    부탁해요("PLEASE"),
    서운해요("SAD")
    ;

    private final String value;

    HeartType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
