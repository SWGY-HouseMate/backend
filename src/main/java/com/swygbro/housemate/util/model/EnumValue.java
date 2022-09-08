package com.swygbro.housemate.util.model;

import lombok.Getter;

/**
 * EnumModel을 프론트로 보낼 때 사용
 */
@Getter
public class EnumValue {
    private final String key;
    private final String value;

    public EnumValue(EnumModel enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
    }

    public EnumValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}