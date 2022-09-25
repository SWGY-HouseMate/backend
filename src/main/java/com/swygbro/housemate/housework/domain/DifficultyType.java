package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum DifficultyType implements EnumModel {
    UPPER("상"),
    MIDDLE("중"),
    LOWER("하")
    ;

    private String value;

    DifficultyType(String value) {
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
