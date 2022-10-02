package com.swygbro.housemate.housework.repository.work;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.swygbro.housemate.housework.domain.QHouseWork.houseWork;

@RequiredArgsConstructor
public class HouseWorkCustomRepositoryImpl implements HouseWorkCustomRepository {

    private final JPQLQueryFactory queryFactory;

    @Override
    public Optional<HouseWork> findByHouseWorkIdJoinManger(String houseWorkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(houseWork)
                        .join(houseWork.manager)
                        .fetchJoin()
                        .where(houseWork.houseWorkId.eq(houseWorkId))
                        .fetchOne());
    }

    @Override
    public List<HouseWork> searchHouseWorkAtDateByGroupDSL(LocalDate startAt, LocalDate endAt, Group group) {
        return queryFactory.selectFrom(houseWork)
                .join(houseWork.manager)
                .fetchJoin()
                .join(houseWork.cycle)
                .fetchJoin()
                .join(houseWork.group)
                .where(houseWork.today.between(startAt, endAt)
                        .and(houseWork.group.eq(group)))
                .fetchAll()
                .fetch();
    }


}
