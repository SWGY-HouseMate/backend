package com.swygbro.housemate.analysis.repository;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseWorkAnalysisRepository extends JpaRepository<HouseWorkAnalysis, String> {
}
