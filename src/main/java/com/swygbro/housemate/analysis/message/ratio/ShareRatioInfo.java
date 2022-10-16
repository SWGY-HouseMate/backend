package com.swygbro.housemate.analysis.message.ratio;

import lombok.*;

@Getter
@ToString
@Value(staticConstructor = "of")
public class ShareRatioInfo {
    String groupId;
    String memberId;
    Integer sum;
}
