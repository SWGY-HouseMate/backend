package com.swygbro.housemate.login.domain;

import com.swygbro.housemate.util.model.EnumModel;

public enum MemberType implements EnumModel {
    DEFAULT("기본"),
    OWNER("관리자")
    ;

    private final String value;

    MemberType(String value) {
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
