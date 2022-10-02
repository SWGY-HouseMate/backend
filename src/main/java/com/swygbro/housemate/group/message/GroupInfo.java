package com.swygbro.housemate.group.message;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {

    private String zipHapGroupId;
    private String groupName;
    private String linkId;
    private MemberInfo owner;

}
