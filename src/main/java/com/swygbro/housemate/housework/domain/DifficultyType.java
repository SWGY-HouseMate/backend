package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum DifficultyType implements EnumModel {
    UPPER(3),
    MIDDLE(2),
    LOWERLOWER(1)
    ;

    private Integer value;

    DifficultyType(Integer value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.name();
    }

    @Override
    public String getValue() {
        return this.value.toString();
    }

    public Integer getScore() {
        return this.value;
    }
}
