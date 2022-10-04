package com.swygbro.housemate.heart.repository.letter;

import com.swygbro.housemate.heart.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterRepository extends JpaRepository<String, Letter> {


}
