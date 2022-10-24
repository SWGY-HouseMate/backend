package com.swygbro.housemate.analysis.message.best_worst;

import lombok.Value;

@Value(staticConstructor = "of")
public class BestWorstInfo {

    BestInfo bestInfo;
    WorstInfo worstInfo;

}
