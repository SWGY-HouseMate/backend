package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.HouseWorkStatusType;
import lombok.Getter;

@Getter
public class HouseWorkCompleted {

    private String house_work_id;
    private HouseWorkStatusType houseWorkStatusType;

}
