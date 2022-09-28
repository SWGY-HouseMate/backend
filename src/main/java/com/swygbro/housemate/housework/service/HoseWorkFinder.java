package com.swygbro.housemate.housework.service;


import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.service.worker.DayOfTheWeekCreator;
import com.swygbro.housemate.housework.service.worker.DaysCreator;
import com.swygbro.housemate.housework.service.worker.HouseWorker;
import com.swygbro.housemate.housework.service.worker.MouthCreator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.swygbro.housemate.housework.domain.CycleType.*;

@Component
public class HoseWorkFinder {

    Map<CycleType, HouseWorker> houseWorkerMap = new LinkedHashMap<>();

    public HoseWorkFinder(DaysCreator daysCreator,
                          DayOfTheWeekCreator dayOfTheWeekCreator,
                          MouthCreator mouthCreator) {
        houseWorkerMap.put(일_마다, daysCreator);
        houseWorkerMap.put(요일_마다, dayOfTheWeekCreator);
        houseWorkerMap.put(매달, mouthCreator);
    }

    public HouseWorker findBy(CycleType cycleType) {
        return houseWorkerMap.get(cycleType);
    }

}
