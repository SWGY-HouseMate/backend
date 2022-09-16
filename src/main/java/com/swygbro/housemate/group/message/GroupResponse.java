package com.swygbro.housemate.group.message;

import lombok.Value;

@Value(staticConstructor = "of")
public class GroupResponse {

     String shortId;
     String createdAt;

}