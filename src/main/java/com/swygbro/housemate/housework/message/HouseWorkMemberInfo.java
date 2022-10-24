package com.swygbro.housemate.housework.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseWorkMemberInfo {

    String memberId;
    String memberEmail;
    String memberLoginRole;
    String memberAuthorityRoles;
    List<HouseWorkInfo> houseWorkInfoList;

}
