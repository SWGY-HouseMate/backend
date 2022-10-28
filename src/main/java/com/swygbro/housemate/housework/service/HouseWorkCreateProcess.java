package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.exception.badrequest.BadRequestException;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.HoseWorkCreate;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.housework.service.cycle.CycleFactory;
import com.swygbro.housemate.login.ManagedFactory;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.swygbro.housemate.exception.badrequest.BadRequestType.그룹에_한_명_밖에_없습니다;

@Service
@RequiredArgsConstructor
public class HouseWorkCreateProcess {

    private final ManagedFactory managedFactory;
    private final CycleFactory cycleFactory;
    private final HoseWorkFinder hoseWorkFinder;
    private final HouseWorkRepository houseWorkRepository;
    private final CycleRepository cycleRepository;
    private final HouseWorkUtil houseWorkUtil;
    private final CurrentMemberUtil currentMemberUtil;

    @Transactional
    public HoseWorkCreate execute(CreateHouseWork createHouseWork) throws ParseException {
        // 그룹에 2명이 있는지 확인
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        if (!currentMemberANDGroupObject.getZipHapGroup().getParticipatingMembers().equals(2)) {
            throw new BadRequestException(그룹에_한_명_밖에_없습니다);
        }

        Boolean condition = houseWorkUtil.getCondition(createHouseWork);
        if (!condition) {
            HouseWork houseWork = houseWorkUtil.createHouseWork(createHouseWork);

            managedFactory.assign(List.of(houseWork));
            HouseWork saveHouseWork = houseWorkRepository.save(houseWork);
            return HoseWorkCreate.of(List.of(saveHouseWork.getHouseWorkId()));
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

        List<String> houseWorkIds = saveHouseWorkList.stream()
                .map(HouseWork::getHouseWorkId)
                .collect(Collectors.toList());

        return HoseWorkCreate.of(houseWorkIds);
    }
}
