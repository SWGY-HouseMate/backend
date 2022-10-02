package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class CycleUtil {

    private final CycleRepository cycleRepository;

    private final HouseWorkRepository houseWorkRepository;

    @Transactional
    public String delete(String cycleId) {
        Cycle cycle = cycleRepository.findByCycleId(cycleId).orElseThrow(null);
        houseWorkRepository.deleteAllByCycle(cycle);

        cycleRepository.deleteByCycleId(cycleId);
        return cycleId;
    }

}
