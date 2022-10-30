package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class PrivateViewMessage {

    private String heartId;
    private String letterId;

    private HeartType heartType;

    private MemberInfo from;
    private MemberInfo to;

    private String createAt;
}
