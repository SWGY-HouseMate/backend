package com.swygbro.housemate.analysis.message.mostmany;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
@ToString
public class MostManyInfo {

    String groupId;
    String memberId;
    String title;
    Boolean isComplete;

}
