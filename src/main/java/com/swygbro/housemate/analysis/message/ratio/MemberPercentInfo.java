package com.swygbro.housemate.analysis.message.ratio;

import lombok.ToString;
import lombok.Value;

@ToString
@Value(staticConstructor = "of")
public class MemberPercentInfo {
    String groupId;

    String memberId;

    double percent;
}
