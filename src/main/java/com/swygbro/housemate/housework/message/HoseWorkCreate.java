package com.swygbro.housemate.housework.message;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class HoseWorkCreate {

    List<String> id;

}
