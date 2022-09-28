package com.swygbro.housemate.housework.service.worker;

import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DaysCreator implements HouseWorker {
    private final UUIDUtil uuidUtil;

    @Override
    public List<HouseWork> createWorks(CreateHouseWork createHouseWork, Long days) {
        List<HouseWork> returnHouseWork = new ArrayList<>();

        int day = (Integer) createHouseWork.getProps().get("additional");
        LocalDate startAt = createHouseWork.getStartAt();

        for (int i = 0; i <= days; i+=day) {
            returnHouseWork.add(createHouseWork(createHouseWork, startAt, i));
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
                .isCompleted(false)
                .build();
    }
}
