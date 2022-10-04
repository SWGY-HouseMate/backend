package com.swygbro.housemate.heart.repository.heart;

import com.swygbro.housemate.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<String, Heart> {

}
