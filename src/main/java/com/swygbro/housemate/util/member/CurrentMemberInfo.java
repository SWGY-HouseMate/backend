package com.swygbro.housemate.util.member;

import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class CurrentMemberInfo {

    GroupInfo groupInfo;
    MemberInfo memberInfo;

}
