package com.swygbro.housemate.group.message;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.Value;

@Value(staticConstructor = "of")
public class GroupResponse {

     String uriId;
     String createdAt;
     String uri;

}