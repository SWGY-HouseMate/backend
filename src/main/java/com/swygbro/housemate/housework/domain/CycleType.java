package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum CycleType implements EnumModel {
    일_마다("몇 일마다"),
    요일_마다("몇 요일 마다"),
    매달("매달")
    ;

    private String value;

    CycleType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }
}
