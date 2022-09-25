package com.swygbro.housemate.login.repository;

import com.swygbro.housemate.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(String memberId);
    Optional<Member> findByMemberEmail(String email);

//    @Query("SELECT m FROM Member m join fetch m.zipHapGroup where m.memberEmail = :email")
//    Member findByMemberEmailJPQL(@Param("email") String email);
}
