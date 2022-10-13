package com.swygbro.housemate.analysis.message;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class ShareRatioInfo {
    String memberId;
    Integer sum;
    String groupId;
}
