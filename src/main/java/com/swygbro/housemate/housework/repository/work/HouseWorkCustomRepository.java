package com.swygbro.housemate.housework.repository.work;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import com.swygbro.housemate.housework.message.HouseWorkCountForGroup;
import com.swygbro.housemate.login.domain.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HouseWorkCustomRepository {
    Optional<HouseWork> searchHouseWorkIdJoinManger(String houseWorkId);
    HouseWorkByMember searchHouseWorkAtDateByGroup(LocalDate startAt, LocalDate endAt, Group group);
    HouseWorkCountForGroup searchHouseWorkCountByMember(LocalDate startAt, LocalDate endAt, Group group);
    List<HouseWork> searchHouseWorkByToday(LocalDate now);
    Long countByMember(LocalDate now, Member member);
    List<HouseWork> searchCalculateMostHouseWork(LocalDate now);
}
