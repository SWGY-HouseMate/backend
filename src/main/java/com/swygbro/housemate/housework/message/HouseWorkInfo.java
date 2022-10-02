package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.DifficultyType;
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
    Boolean isCompleted;
    Boolean isCycle;
    CycleInfo cycleInfo;

}
