package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.repository.CycleRepository;
import com.swygbro.housemate.housework.repository.HouseWorkRepository;
import com.swygbro.housemate.housework.service.cycle.CycleFactory;
import com.swygbro.housemate.login.ManagedFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseWorkCreateProcess {

    private final ManagedFactory managedFactory;
    private final CycleFactory cycleFactory;
    private final HoseWorkFinder hoseWorkFinder;
    private final HouseWorkRepository houseWorkRepository;
    private final CycleRepository cycleRepository;
    private final HouseWorkUtil houseWorkUtil;

    @Transactional
    public HoseWorkRes execute(CreateHouseWork createHouseWork) throws ParseException {
        Boolean condition = houseWorkUtil.getCondition(createHouseWork);

        if (!condition) {
            HouseWork houseWork = houseWorkUtil.createHouseWork(createHouseWork);

            managedFactory.assign(List.of(houseWork));
            houseWorkRepository.save(houseWork);
            return HoseWorkRes.of(null, null);
        }

        // 반복 주기 만큼 DB row 생성
        Cycle cycle = cycleRepository.save(cycleFactory.create(createHouseWork));

        long days = houseWorkUtil.startAtAndEndAtDiff(createHouseWork);
        List<HouseWork> houseWorkerWorks = hoseWorkFinder.findBy(createHouseWork.getCycleType())
                .createWorks(createHouseWork, days);

        for (HouseWork houseWorkerWork : houseWorkerWorks) { // 반복 주기 할당
            houseWorkerWork.setCycle(cycle);
        }

        managedFactory.assign(houseWorkerWorks); // 담당자와 그룹 할당
        houseWorkRepository.saveAll(houseWorkerWorks);

        return HoseWorkRes.of(null, null);
    }
}
