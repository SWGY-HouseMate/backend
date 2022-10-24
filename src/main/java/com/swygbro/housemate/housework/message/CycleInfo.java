package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.CycleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleInfo {

    private String cycleId;
    private CycleType cycleType;
    private String props;
    private LocalDate startAt;
    private LocalDate endAt;

}
