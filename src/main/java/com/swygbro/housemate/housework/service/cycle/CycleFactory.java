package com.swygbro.housemate.housework.service.cycle;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.repository.CycleRepository;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CycleFactory implements CycleFactoryManager {
    private final UUIDUtil uuidUtil;

    @Override
    public Cycle create(CreateHouseWork createHouseWork) {
        return Cycle.builder()
                .cycleId(uuidUtil.create())
                .startAt(createHouseWork.getStartAt())
                .endAt(createHouseWork.getEndAt())
                .cycleType(createHouseWork.getCycleType())
                .props(Map.of("additional", createHouseWork.getProps()).toString())
                .build();
    }

}
