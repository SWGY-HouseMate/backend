package com.swygbro.housemate.login.repository;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;

import java.util.Optional;

public interface MemberCustomRepository {

    Optional<Member> findByEmailJoinFetchGroup(String email);
}
