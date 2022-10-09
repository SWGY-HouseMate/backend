package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.login.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputFirstHeartLetter {

    private HeartType heartType;

    private String title;

    private String content;

    private String from;

    public Heart creatHeartEntity(String heartId, Member to) {
        return Heart.builder()
                .heartId(heartId)
                .to(to)
                .isCreateAllMembers(false)
                .build();
    }

    public Letter createLetterEntity(String letterId, Member from, Heart heart, Group group) {
        return Letter.builder()
                .letterId(letterId)
                .title(this.title)
                .content(this.content)
                .kind(this.heartType)
                .from(from)
                .heart(heart)
                .group(group)
                .build();
    }
}
