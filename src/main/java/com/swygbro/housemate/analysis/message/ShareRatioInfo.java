package com.swygbro.housemate.analysis.message;

import com.swygbro.housemate.group.message.GroupInfo;
import lombok.*;

@Getter
@Value(staticConstructor = "of")
public class ShareRatioInfo {
    String memberId;
    Integer sum;
    String groupId;
}
