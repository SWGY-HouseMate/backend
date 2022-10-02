package com.swygbro.housemate.housework.repository.work;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface HouseWorkCustomRepository {
    Optional<HouseWork> findByHouseWorkIdJoinManger(String houseWorkId);
    HouseWorkByMember searchHouseWorkAtDateByGroupDSL(LocalDate startAt, LocalDate endAt, Group group);
}
