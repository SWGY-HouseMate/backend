package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.heart.domain.Letter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputFirstHeartLetter {

    private HeartType heartType;
    private String content;

    private String from;

    public Heart creatHeartEntity(String heartId) {
        return Heart.builder()
                .heartId(heartId)
                .isCreateAllMembers(false)
                .build();
    }

    public Letter createLetterEntity(String letterId, String from, String to, Heart heart, Group group) {
        return Letter.builder()
                .letterId(letterId)
                .content(this.content)
                .heartType(this.heartType)
                .letterFrom(from)
                .letterTo(to)
                .heart(heart)
                .group(group)
                .build();
    }
}
