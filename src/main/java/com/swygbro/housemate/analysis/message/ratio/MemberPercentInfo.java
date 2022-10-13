package com.swygbro.housemate.analysis.message.ratio;

import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString
public class MemberPercentInfo {

    String groupId;
    String memberId;
    double percent;

}
