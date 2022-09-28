package com.swygbro.housemate.housework.repository;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, String> {

    @Query("select h from HouseWork h " +
            "join fetch h.manager " +
            "where h.houseWorkId = :houseWorkId")
    Optional<HouseWork> findByHouseWorkIdJoinManger(String houseWorkId);

    void deleteAllByCycle(Cycle cycle);

    @Query("select h from HouseWork h " +
            "join fetch h.manager " +
            "join fetch h.cycle " +
            "join fetch h.group " +
            "where h.today between :startAt and :endAt " +
            "and h.group = :group")
    List<HouseWork> searchHouseWorkAtDateByGroup(LocalDate startAt, LocalDate endAt, Group group);

    @Query("select h from HouseWork h " +
            "join fetch h.manager " +
            "join fetch h.cycle " +
            "join fetch h.group " +
            "where h.today between :startAt and :endAt " +
            "and h.manager = :manager")
    List<HouseWork> searchHouseWorkAtDateByManager(LocalDate startAt, LocalDate endAt, Member manager);

}
