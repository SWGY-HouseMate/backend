package com.swygbro.housemate.group.message;

import com.swygbro.housemate.util.member.GroupPersonInfo;
import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class GroupInfoByAll {
    String zipHapGroupId;
    String groupName;
    String linkId;

    GroupPersonInfo groupPersonInfo;
}
