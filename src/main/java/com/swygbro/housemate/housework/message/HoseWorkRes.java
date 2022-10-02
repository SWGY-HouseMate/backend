package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.group.message.GroupInfo;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class HoseWorkRes {

    List<HouseWorkByMember> houseWorkByMembers;
    GroupInfo groupInfo;

}
