package com.swygbro.housemate.login.domain;

import com.swygbro.housemate.util.EnumModel;

public enum LoginType implements EnumModel {
    구글("GOOGLE"),
    카카오("KAKAO"),
    애플("APPLE")
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
