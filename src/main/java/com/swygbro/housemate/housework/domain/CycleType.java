package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum CycleType implements EnumModel {
    일_마다(10),
    요일_마다(6),
    매달(3)
    ;

    private final Integer value;

    CycleType(Integer value) {
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
