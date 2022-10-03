package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.DifficultyType;
import com.swygbro.housemate.housework.domain.HouseWorkStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseWorkInfo {

    String houseWorkId;
    String title;
    DifficultyType difficulty;
    LocalDate today;
    HouseWorkStatusType houseWorkStatusType;
    Boolean isCycle;
    CycleInfo cycleInfo;

}
