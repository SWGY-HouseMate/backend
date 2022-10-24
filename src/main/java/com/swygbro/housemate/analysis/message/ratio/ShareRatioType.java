package com.swygbro.housemate.analysis.message.ratio;

import com.swygbro.housemate.util.model.EnumModel;

public enum ShareRatioType implements EnumModel {
    잘하고_있어요("잘하고_있어요"),
    너무_적어요("너무_적어요"),
    너무_많아요("너무_많아요")
    ;

    private final String value;

    ShareRatioType(String value) {
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
