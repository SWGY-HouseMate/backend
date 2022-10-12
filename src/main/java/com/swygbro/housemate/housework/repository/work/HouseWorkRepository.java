package com.swygbro.housemate.housework.repository.work;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, String>, HouseWorkCustomRepository {
    void deleteAllByCycle(Cycle cycle);
}
