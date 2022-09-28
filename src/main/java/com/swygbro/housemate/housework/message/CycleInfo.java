package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.CycleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CycleInfo {

    private String cycleId;
    private CycleType cycleType;
    private String props;
    private LocalDate startAt;
    private LocalDate endAt;

}
