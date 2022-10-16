package com.swygbro.housemate.analysis.message.mostmany;

import lombok.ToString;
import lombok.Value;

import java.util.Map;

@ToString
@Value(staticConstructor = "of")
public class MemberCountInfo {

    String groupId;

    String memberId;

    Map<String, Integer> titleCount;
}
