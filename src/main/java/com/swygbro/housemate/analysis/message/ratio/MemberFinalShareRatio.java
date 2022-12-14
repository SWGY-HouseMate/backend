package com.swygbro.housemate.analysis.message.ratio;

import lombok.Value;

@Value(staticConstructor = "of")
public class MemberFinalShareRatio {
    String groupId;

    String memberId;

    ShareRatioType shareRatioType;

    double percent;
}
