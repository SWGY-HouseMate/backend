package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.repository.CycleRepository;
import com.swygbro.housemate.housework.repository.HouseWorkRepository;
import com.swygbro.housemate.housework.service.cycle.CycleFactory;
import com.swygbro.housemate.login.ManagedFactory;
import com.swygbro.housemate.util.condition.CycleCondition;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseWorkProcess {

    private final ManagedFactory managedFactory;
    private final CycleFactory cycleFactory;
    private final HoseWorkFinder hoseWorkFinder;
    private final CycleCondition cycleCondition;
    private final HouseWorkRepository houseWorkRepository;
    private final CycleRepository cycleRepository;
    private final UUIDUtil uuidUtil;

    @Transactional
    public HoseWorkRes execute(CreateHouseWork createHouseWork) throws ParseException {
        Boolean condition = getCondition(createHouseWork);

        if (!condition) {
            HouseWork houseWork = createHouseWork(createHouseWork);

            managedFactory.assign(List.of(houseWork));
            houseWorkRepository.save(houseWork);
            return HoseWorkRes.of("단일 집안일 생성 완료");
        }

        // 반복 주기 만큼 DB row 생성
        Cycle cycle = cycleRepository.save(cycleFactory.create(createHouseWork));

        long days = startAtAndEndAtDiff(createHouseWork);
        List<HouseWork> houseWorkerWorks = hoseWorkFinder.findBy(createHouseWork.getCycleType())
                .createWorks(createHouseWork, days);

        for (HouseWork houseWorkerWork : houseWorkerWorks) { // 반복 주기 할당
            houseWorkerWork.setCycle(cycle);
        }

        managedFactory.assign(houseWorkerWorks); // 담당자와 그룹 할당
        houseWorkRepository.saveAll(houseWorkerWorks);

        return HoseWorkRes.of("반복 주기 및 집안일 생성 완료");
    }

    private HouseWork createHouseWork(CreateHouseWork createHouseWork) {
        return houseWorkRepository.save(HouseWork.builder()
                .houseWorkId(uuidUtil.create())
                .title(createHouseWork.getTitle())
                .difficulty(createHouseWork.getDifficulty())
                .isCycle(createHouseWork.getIsCycle())
                .date(createHouseWork.getToday())
                .isCompleted(false)
                .build());
    }

    private long startAtAndEndAtDiff(CreateHouseWork createHouseWork) throws ParseException {
        LocalDate startAt = createHouseWork.getStartAt();
        LocalDate endAt = createHouseWork.getEndAt();

        Date startDate = dateConverter(startAt);
        Date endDate = dateConverter(endAt);

        long differenceInMillis = endDate.getTime() - startDate.getTime();
        return  (differenceInMillis / (24 * 60 * 60 * 1000));
    }

    private Date dateConverter(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        return date;
    }

    private Boolean getCondition(CreateHouseWork createHouseWork) {
        return cycleCondition.isSatisfiedBy(createHouseWork);
    }
}
