package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Value;

@Value(staticConstructor = "of")
public class BestInfo {

    String groupId;

    String memberId;

    String houseWorkId;

}