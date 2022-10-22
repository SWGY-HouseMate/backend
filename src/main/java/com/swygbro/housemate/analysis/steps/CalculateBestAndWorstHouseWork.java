package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.best_worst.BestAndWorstScore;
import com.swygbro.housemate.analysis.message.best_worst.BestInfo;
import com.swygbro.housemate.analysis.message.best_worst.WorstInfo;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.analysis.util.dto.AnalysisDto;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateBestAndWorstHouseWork {

    private final AnalysisUtil analysisUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final CycleRepository cycleRepository;

    @Bean
    public Step executeByHouseWork() {
        return stepBuilderFactory.get("CalculateBestAndWorstHouseWorkStep")
                .tasklet((contribution, chunkContext) -> {

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.removeOnlyOneMemberInTheGroup(
                            houseWorkRepository.searchCalculateMostHouseWork(now)
                    );

                    // 주기가 없는 것은 거르기
                    // 한달에 3번 미만은 거르기


                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    log.info("======= 난위도에 따른 점수 구하기 =======");
                    Map<Group, List<HouseWork>> groupingGroup = houseWorkList.stream()
                            .collect(Collectors.groupingBy(HouseWork::getGroup));

                    for (Group group : groupingGroup.keySet()) {
                        List<HouseWork> findByMember = groupingGroup.get(group);
                        for (HouseWork houseWork : findByMember) {
                            Integer score = houseWork.getDifficulty().getScore();



                        }
                    }


                    log.info("======= 반복에 따른 점수 구하기 =======");

                    log.info("======= 성공률 구하기 =======");

                    log.info("======= 합산 =======");

                    log.info("======= 가장 잘한 일 =======");
                    BestInfo.of("", "", "");

                    log.info("======= 담당자 변경이 필요한 일 =======");
                    WorstInfo.of("", "", "");

                    log.info("======= DB 저장 =======");


                    return FINISHED;
                }).build();
    }

}
