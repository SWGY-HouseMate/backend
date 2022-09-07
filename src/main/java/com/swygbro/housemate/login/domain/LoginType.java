package com.swygbro.housemate.login.domain;

import com.swygbro.housemate.util.EnumModel;

public enum LoginType implements EnumModel {
    GOOGLE("구글"),
    KAKAO("카카오"),
    APPLE("애플")
    ;

    private final String value;

    LoginType(String value) {
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
