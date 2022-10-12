package com.swygbro.housemate.login.repository;

import com.swygbro.housemate.login.domain.Member;

import java.util.Optional;

public interface MemberCustomRepository {

    Optional<Member> findByEmailJoinFetchGroup(String email);

    Optional<Member> findByIdJoinFetchGroup(String memberId);
}
