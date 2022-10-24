package com.swygbro.housemate.login.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.swygbro.housemate.login.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<Member> findByEmailJoinFetchGroup(String email) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .join(member.zipHapGroup).fetchJoin()
                .where(member.memberEmail.eq(email))
                .fetchOne());
    }

    @Override
    public Optional<Member> findByIdJoinFetchGroup(String memberId) {
        return Optional.ofNullable(queryFactory.selectFrom(member)
                .join(member.zipHapGroup).fetchJoin()
                .where(member.memberId.eq(memberId))
                .fetchOne());
    }
}
