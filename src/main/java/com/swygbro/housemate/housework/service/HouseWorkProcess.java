package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.CycleType;
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
    private final HouseWorkRepository houseWorkRepository;
    private final UUIDUtil uuidUtil;
    private final CycleCondition cycleCondition;
    private final CycleFactory cycleFactory;
    private final HoseWorkFinder hoseWorkFinder;
    private final CycleRepository cycleRepository;


    @Transactional
    public HoseWorkRes execute(CreateHouseWork createHouseWork) throws ParseException {
        Boolean condition = getCondition(createHouseWork);

        if (!condition) {
            HouseWork houseWork = houseWorkRepository.save(HouseWork.builder()
                    .houseWorkId(uuidUtil.create())
                    .title(createHouseWork.getTitle())
                    .difficulty(createHouseWork.getDifficulty())
                    .isCycle(createHouseWork.getIsCycle())
                    .date(createHouseWork.getToday())
                    .isCompleted(false)
                    .build());

            managedFactory.assign(List.of(houseWork));
            return HoseWorkRes.of("단일 집안일 생성 완료");
        }

        // 반복 주기 만큼 DB row 생성
        long days = getDays(createHouseWork);

        CycleType cycleType = createHouseWork.getCycleType();
        HouseWorker houseWorker = hoseWorkFinder.findBy(cycleType);
        List<HouseWork> houseWorkerWorks = houseWorker.createWorks(createHouseWork, days);
        houseWorkRepository.saveAll(houseWorkerWorks);

        Cycle cycle = cycleFactory.create(createHouseWork);
        for (HouseWork houseWorkerWork : houseWorkerWorks) {
            cycle.setHouseWork(houseWorkerWork);
        }

        cycleRepository.save(cycle);

        managedFactory.assign(houseWorkerWorks);
        return HoseWorkRes.of("반복 주기 생성 완료");
    }

    private long getDays(CreateHouseWork createHouseWork) throws ParseException {
        LocalDate startAt = createHouseWork.getStartAt();
        LocalDate endAt = createHouseWork.getEndAt();

        Date startDate = getDate(startAt);
        Date endDate = getDate(endAt);

        long differenceInMillis = endDate.getTime() - startDate.getTime();
        return  (differenceInMillis / (24 * 60 * 60 * 1000));
    }

    private Date getDate(LocalDate localDate) {
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
