package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.util.condition.CycleCondition;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class HouseWorkUtil {
    private final CycleCondition cycleCondition;
    private final UUIDUtil uuidUtil;

    public Boolean getCondition(CreateHouseWork createHouseWork) {
        return cycleCondition.isSatisfiedBy(createHouseWork);
    }

    public long startAtAndEndAtDiff(CreateHouseWork createHouseWork) throws ParseException {
        LocalDate startAt = createHouseWork.getStartAt();
        LocalDate endAt = createHouseWork.getEndAt();

        Date startDate = dateConverter(startAt);
        Date endDate = dateConverter(endAt);

        long differenceInMillis = endDate.getTime() - startDate.getTime();
        return  (differenceInMillis / (24 * 60 * 60 * 1000));
    }

    public Date dateConverter(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        return date;
    }

    public HouseWork createHouseWork(CreateHouseWork createHouseWork) {
        return HouseWork.builder()
                .houseWorkId(uuidUtil.create())
                .title(createHouseWork.getTitle())
                .difficulty(createHouseWork.getDifficulty())
                .isCycle(createHouseWork.getIsCycle())
                .today(createHouseWork.getToday())
                .isCompleted(false)
                .build();
    }
}
