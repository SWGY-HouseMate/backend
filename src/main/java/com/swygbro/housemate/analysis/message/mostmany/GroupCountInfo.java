package com.swygbro.housemate.analysis.message.mostmany;

import lombok.ToString;
import lombok.Value;

import java.util.Map;

@ToString
@Value(staticConstructor = "of")
public class GroupCountInfo {

    String groupId;

    Map<String, Integer> titleCount;
}
