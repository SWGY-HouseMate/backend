package com.swygbro.housemate.housework.service.worker;

import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.service.HouseWorker;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        for (int i = 1; i <= days; i++) {
            int finalI = i;
            Arrays.stream(dayOfTheWeekSplit).forEach(s -> {
                if(s.equals(createHouseWork.getStartAt().plusDays(finalI).getDayOfWeek().getDisplayName(FULL, KOREAN))) {
                    returnHouseWork.add(createHouseWork(createHouseWork, createHouseWork.getStartAt(), finalI));
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
                .date(startAt.plusDays(i))
                .isCompleted(false)
                .build();
    }
}
