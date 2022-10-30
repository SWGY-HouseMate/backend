package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SumScore {
    private String groupId;

    private String memberId;

    private String houseWorkTitle;

    private Double sum;
}
