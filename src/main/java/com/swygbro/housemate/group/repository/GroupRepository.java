package com.swygbro.housemate.group.repository;

import com.swygbro.housemate.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupCustomRepository {
    Optional<Group> findByLinkId(String linkId);

    Optional<Group> findByZipHapGroupId(String groupId);
}
