package com.swygbro.housemate.group.repository;

import com.swygbro.housemate.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByLinkId(String linkId);

    @Query("select g from Group g join fetch g.owner where g.linkId = :linkId")
    Optional<Group> findByLinkIdQuery(String linkId);
}
