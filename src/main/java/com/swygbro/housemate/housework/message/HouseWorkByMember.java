package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.group.message.GroupInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseWorkByMember {

    List<HouseWorkMemberInfo> houseWorkInfos;
    GroupInfo groupInfo;

}
