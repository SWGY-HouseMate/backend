package com.swygbro.housemate.heart.repository.letter;

import com.swygbro.housemate.heart.domain.Heart;
import com.swygbro.housemate.heart.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LetterRepository extends JpaRepository<Letter, String>, LetterCustomRepository {
    Optional<Letter> findByHeart(Heart heart);
}
