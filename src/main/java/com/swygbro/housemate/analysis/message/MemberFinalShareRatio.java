package com.swygbro.housemate.analysis.message;

import lombok.Value;

@Value(staticConstructor = "of")
public class MemberFinalShareRatio {

    String groupId;

    String memberId;

    double percent;

    ShareRatioType shareRatioType;
}
