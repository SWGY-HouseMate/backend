package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class BestAndWorstScore {
    String groupId;

    String memberId;

    Map<String, Integer> bestScore;
}
