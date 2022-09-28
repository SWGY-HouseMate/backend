package com.swygbro.housemate.housework.repository;

import com.swygbro.housemate.housework.domain.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, String> {
    void deleteByCycleId(String cycleId);

    Optional<Cycle> findByCycleId(String cycleId);
}
