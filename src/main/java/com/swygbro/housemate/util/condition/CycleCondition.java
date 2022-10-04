package com.swygbro.housemate.util.condition;

import com.swygbro.housemate.housework.message.CreateHouseWork;
import org.springframework.stereotype.Component;

@Component
public class CycleCondition implements Condition<CreateHouseWork> {
    @Override
    public Boolean isSatisfiedBy(CreateHouseWork createHouseWork) {
        return createHouseWork.getIsCycle();
    }
}
