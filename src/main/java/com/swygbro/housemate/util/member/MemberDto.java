package com.swygbro.housemate.util.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private String memberId;

    private String memberEmail;

    private String memberName;

    private String memberProfilePicture;

    private String memberLoginRole;
}
