package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;

import java.util.List;

public interface HouseWorker {
    List<HouseWork> createWorks(CreateHouseWork createHouseWork, Long days);
}
