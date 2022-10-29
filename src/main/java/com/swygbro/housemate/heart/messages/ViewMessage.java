package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class ViewMessage {

    private String heartId;
    private String letterId;

    private String title;
    private String content;
    private HeartType kind;
    private MemberInfo from;
    private MemberInfo to;

    private String createAt;
}
