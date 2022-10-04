package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum HouseWorkStatusType implements EnumModel {
    DEFAULT("기본값"),
    COMPLETED("완료함"),
    FAILURE("안함"),
    DELAY("미룸");

    private final String value;

    HouseWorkStatusType(String value) {
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
