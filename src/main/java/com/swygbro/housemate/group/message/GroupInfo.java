package com.swygbro.housemate.group.message;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {

    private String zipHapGroupId;
    private String groupName;
    private String linkId;
    private MemberInfo owner;

}
