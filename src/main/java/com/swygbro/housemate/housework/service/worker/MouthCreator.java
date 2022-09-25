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
import java.util.List;

@Component
@RequiredArgsConstructor
public class MouthCreator implements HouseWorker {

    private final UUIDUtil uuidUtil;

    @Override // TODO: 이것만 성공하면 됨.
    public List<HouseWork> createWorks(CreateHouseWork createHouseWork, Long days) {
        List<HouseWork> returnHouseWork = new ArrayList<>();

        int monthOfLoop = createHouseWork.getEndAt().getMonthValue() - createHouseWork.getStartAt().getMonthValue();
        for (int i = 1; i <= monthOfLoop; i++) {
            int mouth = createHouseWork.getStartAt().getMonthValue() + i;
            returnHouseWork.add(createHouseWork(createHouseWork, createHouseWork.getStartAt(), i));
        }

        return returnHouseWork;
    }

    private HouseWork createHouseWork(CreateHouseWork createHouseWork, LocalDate startAt, int i) {
        return HouseWork.builder()
                .houseWorkId(uuidUtil.create())
                .title(createHouseWork.getTitle())
                .difficulty(createHouseWork.getDifficulty())
                .isCycle(createHouseWork.getIsCycle())
                .date(createHouseWork.getStartAt().plusDays(i))
                .isCompleted(false)
                .build();
    }
}
