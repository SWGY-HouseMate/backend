package com.swygbro.housemate.analysis.repository;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HouseWorkAnalysisRepository extends JpaRepository<HouseWorkAnalysis, String> {
    Optional<HouseWorkAnalysis> findByTodayAndMemberId(LocalDate now, String memberId);
    List<HouseWorkAnalysis> findByTodayAndGroupId(LocalDate now, String groupId);
    List<HouseWorkAnalysis> findByGroupId(String groupId);
}
