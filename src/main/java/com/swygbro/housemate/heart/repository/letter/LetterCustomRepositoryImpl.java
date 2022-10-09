package com.swygbro.housemate.heart.repository.letter;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.heart.domain.Letter;
import com.swygbro.housemate.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.swygbro.housemate.heart.domain.QLetter.letter;

@Repository
@RequiredArgsConstructor
public class LetterCustomRepositoryImpl implements LetterCustomRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public List<Letter> findByUserIdAndRead(Member to) {
        return queryFactory.selectFrom(letter)
                .join(letter.heart).fetchJoin()
                .where(letter.heart.isCreateAllMembers.eq(false))
                .where(letter.heart.to.eq(to))
                .fetchAll().fetch();
    }

    public Letter findByLetterIdFetch(String letterId) {
        return queryFactory.selectFrom(letter)
                .join(letter.heart).fetchJoin()
                .join(letter.from).fetchJoin()
                .where(letter.letterId.eq(letterId))
                .fetchOne();
    }
}
