package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.heart.domain.Letter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputSecondHeartLetter {
    private HeartType heartType;
    private String content;

    public Letter createLetterEntity(String letterId, String from, String to, Heart heart, Group group) {
        return Letter.builder()
                .letterId(letterId)
                .title(null)
                .content(this.content)
                .heartType(this.heartType)
                .letterFrom(from)
                .letterTo(to)
                .heart(heart)
                .group(group)
                .build();
    }
}
