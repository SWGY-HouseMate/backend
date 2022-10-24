package com.swygbro.housemate.analysis.repository;

import com.swygbro.housemate.analysis.domain.TableLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableLockRepository extends JpaRepository<TableLock, String> {

    Optional<TableLock> findByInstanceId(String instanceId);

}
