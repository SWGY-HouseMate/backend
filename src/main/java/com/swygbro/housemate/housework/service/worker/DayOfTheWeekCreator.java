package com.swygbro.housemate.housework.service.worker;

import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.DEFAULT;
import static java.time.format.TextStyle.FULL;
import static java.util.Locale.KOREAN;

@Component
@RequiredArgsConstructor
public class DayOfTheWeekCreator implements HouseWorker {

    private final UUIDUtil uuidUtil;

    @Override
    public List<HouseWork> createWorks(CreateHouseWork createHouseWork, Long days) {
        List<HouseWork> returnHouseWork = new ArrayList<>();

        String dayOfTheWeek = (String) createHouseWork.getProps().get("additional");
        String[] dayOfTheWeekSplit = dayOfTheWeek.split(",");
        LocalDate startAt = createHouseWork.getStartAt();

        for (int i = 0; i <= days; i++) {
            int finalI = i;
            Arrays.stream(dayOfTheWeekSplit).forEach(s -> {
                if(s.equals(startAt.plusDays(finalI).getDayOfWeek().getDisplayName(FULL, KOREAN))) {
                    returnHouseWork.add(createHouseWork(createHouseWork, startAt, finalI));
                }
            });
        }

        return returnHouseWork;
    }

    private HouseWork createHouseWork(CreateHouseWork createHouseWork, LocalDate startAt, int i) {
        return HouseWork.builder()
                .houseWorkId(uuidUtil.create())
                .title(createHouseWork.getTitle())
                .difficulty(createHouseWork.getDifficulty())
                .isCycle(createHouseWork.getIsCycle())
                .today(startAt.plusDays(i))
                .houseWorkStatusType(DEFAULT)
                .build();
    }
}
