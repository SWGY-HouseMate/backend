package com.swygbro.housemate.group.message;

import com.swygbro.housemate.login.message.MemberInfo;
import lombok.*;

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
