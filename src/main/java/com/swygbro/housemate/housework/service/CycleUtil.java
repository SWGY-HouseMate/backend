package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.반복_주기를_찾을_수_없습니다;

@Component
@RequiredArgsConstructor
public class CycleUtil {

    private final CycleRepository cycleRepository;

    private final HouseWorkRepository houseWorkRepository;

    @Transactional
    public String delete(String cycleId) {
        Cycle cycle = cycleRepository.findByCycleId(cycleId)
                .orElseThrow(() -> new DataNotFoundException(반복_주기를_찾을_수_없습니다));
        houseWorkRepository.deleteAllByCycle(cycle);

        cycleRepository.deleteByCycleId(cycleId);
        return cycleId;
    }

}
