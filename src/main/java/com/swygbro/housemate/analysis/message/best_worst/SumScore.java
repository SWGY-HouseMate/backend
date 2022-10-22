package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SumScore {
    private String groupId;

    private String houseWorkTitle;

    private Double sum;

    public void divided(double scoreToRepetition) {
        this.sum  = this.sum / scoreToRepetition;
    }
}
