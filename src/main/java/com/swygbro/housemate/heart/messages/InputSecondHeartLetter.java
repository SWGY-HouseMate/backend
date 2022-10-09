package com.swygbro.housemate.heart.messages;

import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.HeartType;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.login.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InputSecondHeartLetter {

    private HeartType heartType;

    private String title;

    private String content;

    public Letter createLetterEntity(String letterId, Member from, Heart heart) {
        return Letter.builder()
                .letterId(letterId)
                .title(this.title)
                .content(this.content)
                .kind(this.heartType)
                .from(from)
                .heart(heart)
                .build();
    }
}
