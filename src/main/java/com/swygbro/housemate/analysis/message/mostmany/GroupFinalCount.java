package com.swygbro.housemate.analysis.message.mostmany;

import lombok.ToString;
import lombok.Value;

@ToString
@Value(staticConstructor = "of")
public class GroupFinalCount {

    String groupId;

    String title;

    Integer count;

}
