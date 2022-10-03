package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseWorkCountInfo {

    MemberInfo memberInfo;
    Long allCount;
    Long completedCount;

}
