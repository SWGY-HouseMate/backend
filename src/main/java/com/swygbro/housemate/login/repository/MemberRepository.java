package com.swygbro.housemate.login.repository;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByMemberEmail(String email);

    List<Member> findByZipHapGroup(Group group);
}
