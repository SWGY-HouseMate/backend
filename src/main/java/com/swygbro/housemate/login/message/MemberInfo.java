package com.swygbro.housemate.login.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfo {

    private String memberId;
    private String memberName;
    private String memberEmail;
    private String memberLoginRole;
    private String memberAuthorityRoles;

}
