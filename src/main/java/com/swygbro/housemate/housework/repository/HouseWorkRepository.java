package com.swygbro.housemate.housework.repository;

import com.swygbro.housemate.housework.domain.HouseWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseWorkRepository extends JpaRepository<HouseWork, String> {

}
