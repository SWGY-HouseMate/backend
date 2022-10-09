package com.swygbro.housemate.heart.messages;

import lombok.Value;

@Value(staticConstructor = "of")
public class CreateHeartLetter {

    String heartId;

}
