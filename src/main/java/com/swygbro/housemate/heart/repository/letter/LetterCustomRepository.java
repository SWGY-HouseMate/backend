package com.swygbro.housemate.heart.repository.letter;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.login.domain.Member;

import java.util.List;

public interface LetterCustomRepository {
    List<Letter> findByUserIdAndNotRead(Member to);
    List<Letter> findByGroupAndRead(Group group);
}