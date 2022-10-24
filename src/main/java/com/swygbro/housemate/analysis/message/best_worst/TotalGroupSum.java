package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TotalGroupSum {
    String groupId;

    BestInfo bestInfo;

    WorstInfo worstInfo;
}
