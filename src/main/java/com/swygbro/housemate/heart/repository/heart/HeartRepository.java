package com.swygbro.housemate.heart.repository.heart;

import com.swygbro.housemate.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, String> {
    Optional<Heart> findByHeartId(String heartId);
}
