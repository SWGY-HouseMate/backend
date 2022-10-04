package com.swygbro.housemate.housework.service.cycle;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.message.CreateHouseWork;

public interface CycleFactoryManager {

    Cycle create(CreateHouseWork createHouseWork);

}
