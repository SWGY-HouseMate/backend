package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.best_worst.BestInfo;
import com.swygbro.housemate.analysis.message.best_worst.WorstInfo;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateBestAndWorstHouseWork {

    private final AnalysisUtil analysisUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;

    @Bean
    public Step executeByHouseWork() { // 집안일 리스트를 가져왔는데 그룹내에 멤버가 1명밖에 없으면 어떻게 할껀가?
        return stepBuilderFactory.get("CalculateShareRatioByGroupStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======= 필요한 집안일 가져오기 =======");
                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.validation(
                            houseWorkRepository.searchCalculateMostHouseWork(now)
                    );

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    // 성공률 구하기

                    // 반복에 따른 점수

                    // 난위도에 따른 점수 부여

                    // 합산

                    // 가장 잘한일
                    BestInfo.of("", "", "");

                    // 가장 못한일
                    WorstInfo.of("", "", "");

                    // DB 업데이트

                    return FINISHED;
                }).build();
    }

}
