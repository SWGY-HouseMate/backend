package com.swygbro.housemate.group.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.group.domain.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.swygbro.housemate.group.domain.QGroup.group;

@Repository
@RequiredArgsConstructor
public class GroupCustomRepositoryImpl implements GroupCustomRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<Group> findByLinkIdJoinFetchOwner(String linkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(group)
                        .join(group.owner).fetchJoin()
                        .where(group.linkId.eq(linkId))
                        .fetchOne());
    }
}
