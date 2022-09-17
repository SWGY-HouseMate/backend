package com.swygbro.housemate.group.message;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.login.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreator {

    private String currentMemberId;
    private String groupName;

    public Group create(String id, String linkId, Member owner) {
        return Group.builder()
                .groupId(id)
                .linkId(linkId)
                .owner(owner)
                .name(groupName)
                .build();
    }

}