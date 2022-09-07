package com.swygbro.housemate.util;

public interface EnumModel {

    default EnumValue getEnumValue(){
        return new EnumValue(getKey(), getValue());
    }

    String getKey();

    String getValue();
}