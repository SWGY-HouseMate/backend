package com.swygbro.housemate.util.member;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.Value;

@Value(staticConstructor = "of")
public class GroupPersonInfo {

    MemberInfo me;
    MemberInfo author;

}
