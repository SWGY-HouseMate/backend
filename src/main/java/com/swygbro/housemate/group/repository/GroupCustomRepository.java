package com.swygbro.housemate.group.repository;

import com.swygbro.housemate.group.domain.Group;

import java.util.Optional;

public interface GroupCustomRepository {
    Optional<Group> findByLinkIdJoinFetchOwner(String linkId);
}
