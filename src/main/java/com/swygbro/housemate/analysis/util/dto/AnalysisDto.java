package com.swygbro.housemate.analysis.util.dto;

import com.swygbro.housemate.housework.domain.DifficultyType;
import com.swygbro.housemate.housework.domain.HouseWorkStatusType;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Getter
@ToString
@Value(staticConstructor = "of")
public class AnalysisDto {

    String houseWorkId;

    String groupId;

    String memberId;

    String title;

    HouseWorkStatusType houseWorkStatusType;

    DifficultyType difficulty;

    LocalDate today;

    Boolean isCycle;

    String cycleId;
}
