package com.swygbro.housemate.housework.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.domain.DifficultyType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
public class CreateHouseWork {

    private String title;
    private Boolean isCycle;
    private DifficultyType difficulty;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate today;

    private CycleType cycleType;
    private Map<String, Object> props;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endAt;

}
