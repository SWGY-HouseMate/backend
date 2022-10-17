package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.best_worst.BestInfo;
import com.swygbro.housemate.analysis.message.best_worst.WorstInfo;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.analysis.util.dto.AnalysisDto;
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
    public Step executeByHouseWork() {
        return stepBuilderFactory.get("CalculateShareRatioByGroupStep")
                .tasklet((contribution, chunkContext) -> {

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.removeOnlyOneMemberInTheGroup(
                            houseWorkRepository.searchCalculateMostHouseWork(now)
                    );

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    log.info("======= HouseWork DTO 로 변환 =======");
                    List<AnalysisDto> analysisDtoList = analysisUtil.converterHouseWorkDto(houseWorkList);

                    log.info("======= 성공률 구하기 =======");
                    for (AnalysisDto analysisDto : analysisDtoList) {
                        System.out.println("analysisDto = " + analysisDto);
                    }

                    log.info("======= 반복에 따른 점수 구하기 =======");

                    log.info("======= 난위도에 따른 점수 구하기 =======");

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
