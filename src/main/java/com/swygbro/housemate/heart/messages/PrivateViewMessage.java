package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class PrivateViewMessage {

    private String heartId;
    private String letterId;

    private MemberInfo from;
    private MemberInfo to;

}
