package com.swygbro.housemate.util.member;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class MemberDto {

    String memberId;

    String memberEmail;

    String memberName;

    String memberProfilePicture;

    String memberLoginRole;

    String memberRole;
}
