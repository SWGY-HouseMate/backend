package com.swygbro.housemate.heart.repository.letter;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.heart.domain.Letter;

import java.util.List;

public interface LetterCustomRepository {
    List<Letter> findByUserIdAndNotRead(String from);
    List<Letter> findByGroupAndRead(Group group);
}