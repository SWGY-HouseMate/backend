package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.CycleInfo;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.message.HouseWorkInfo;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.housework.service.cycle.CycleFactory;
import com.swygbro.housemate.login.ManagedFactory;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
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
    private final ModelMapper mapper;

    @Transactional
    public HoseWorkRes execute(CreateHouseWork createHouseWork) throws ParseException {
        Boolean condition = houseWorkUtil.getCondition(createHouseWork);

        List<HouseWorkInfo> houseWorkInfos = new ArrayList<>();
        if (!condition) {
            HouseWork houseWork = houseWorkUtil.createHouseWork(createHouseWork);

            managedFactory.assign(List.of(houseWork));
            HouseWork saveHouseWork = houseWorkRepository.save(houseWork);

            MemberInfo memberInfo = mapper.map(saveHouseWork.getManager(), MemberInfo.class);
            CycleInfo cycleInfo = mapper.map(saveHouseWork.getCycle(), CycleInfo.class);

            houseWorkInfos.add(HouseWorkInfo.of(
                    saveHouseWork.getHouseWorkId(),
                    saveHouseWork.getTitle(),
                    saveHouseWork.getDifficulty(),
                    saveHouseWork.getToday(),
                    saveHouseWork.getIsCompleted(),
                    saveHouseWork.getIsCycle(),
                    cycleInfo, memberInfo));

            GroupInfo groupInfo = mapper.map(saveHouseWork.getGroup(), GroupInfo.class);
            return HoseWorkRes.of(houseWorkInfos, groupInfo);
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
        List<HouseWork> saveHouseWorkList = houseWorkRepository.saveAll(houseWorkerWorks);

        for (HouseWork houseWork : saveHouseWorkList) {
            MemberInfo memberInfo = mapper.map(houseWork.getManager(), MemberInfo.class);
            CycleInfo cycleInfo = mapper.map(houseWork.getCycle(), CycleInfo.class);

            houseWorkInfos.add(HouseWorkInfo.of(
                    houseWork.getHouseWorkId(),
                    houseWork.getTitle(),
                    houseWork.getDifficulty(),
                    houseWork.getToday(),
                    houseWork.getIsCompleted(),
                    houseWork.getIsCycle(),
                    cycleInfo, memberInfo));
        }

        GroupInfo groupInfo = mapper.map(saveHouseWorkList.get(0).getGroup(), GroupInfo.class);
        return HoseWorkRes.of(houseWorkInfos, groupInfo);
    }
}
