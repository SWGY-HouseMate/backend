package com.swygbro.housemate.analysis.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.analysis.message.best_worst.*;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.CycleType;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.swygbro.housemate.housework.domain.CycleType.요일_마다;
import static com.swygbro.housemate.housework.domain.CycleType.일_마다;
import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.COMPLETED;
import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateBestAndWorstHouseWork {

    private final AnalysisUtil analysisUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public Step executeByHouseWork() {
        return stepBuilderFactory.get("CalculateBestAndWorstHouseWorkStep")
                .tasklet((contribution, chunkContext) -> {

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.removeOnlyOneMemberInTheGroup(
                            houseWorkRepository.searchCalculateMostHouseWork(now)
                    );

                    // 주기가 없는 것은 거르기
                    houseWorkList.removeIf(houseWork -> !houseWork.getIsCycle());

                    // 한달에 4번 미만은 거르기
                    Map<String, List<HouseWork>> groupIngTile = houseWorkList.stream().collect(Collectors.groupingBy(HouseWork::getTitle));
                    for (String title : groupIngTile.keySet()) {
                        List<HouseWork> findByTitle = groupIngTile.get(title);

                        if (findByTitle.size() <= 4) {
                            for (HouseWork houseWork : findByTitle) {
                                houseWorkList.remove(houseWork);
                            }
                        }
                    }

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    log.info("======= 집안일 난위도에 따른 점수 구하기 =======");
                    List<ScoreToDifficulty> difficultyScore = getDifficultyScore(houseWorkList);

                    log.info("======= 성공률 구하기 =======");
                    List<HouseWorkSuccessRate> repetitionScore = getRepetitionScore(houseWorkList);

                    log.info("======= 반복에 따른 점수 구하기 ======= -> 이게 애매하다");
                    List<ScoreToRepetition> scoreToRepetitionList = new ArrayList<>();
                    for (HouseWork houseWork : houseWorkList) {
                        Cycle cycle = houseWork.getCycle();
                        CycleType cycleType = cycle.getCycleType();

                        Integer day = null;
                        List<String> weekList = new ArrayList<>();
                        Integer mouth = null;

                        Map<String, Object> map = objectMapper.readValue(cycle.getProps(), Map.class);
                        if (cycleType.equals(일_마다)) {
                            day = (Integer) map.get("additional");
                        } else if (cycleType.equals(요일_마다)) {
                            String weekString = (String) map.get("additional");
                            weekString.split(",");
                            weekList.add(weekString.split(",").toString());
                        } else {
                            mouth = (Integer) map.get("additional");
                        }

                        // 점수 구하기




                        Integer score = cycleType.getScore();

                        //
                    }

                    log.info("======= 합산 =======");


                    log.info("======= 가장 잘한 일 =======");
                    BestInfo.of("", "", "");

                    log.info("======= 담당자 변경이 필요한 일 =======");
                    WorstInfo.of("", "", "");

                    log.info("======= DB 저장 =======");

                    return FINISHED;
                }).build();
    }

    private List<HouseWorkSuccessRate> getRepetitionScore(List<HouseWork> houseWorkList) {
        List<HouseWorkSuccessRate> houseWorkSuccessRateList = new ArrayList<>();
        for (HouseWork houseWork : houseWorkList) {
            Optional<HouseWorkSuccessRate> optionalHouseWorkSuccessRate = houseWorkSuccessRateList.stream()
                    .filter(houseWorkSuccessRate -> houseWorkSuccessRate.getHouseWorkTitle().equals(houseWork.getTitle()))
                    .findFirst();

            if (optionalHouseWorkSuccessRate.isEmpty()) {
                // 전체 일 구하기
                long count = houseWorkList.stream()
                        .filter(houseWorkAll -> houseWork.getTitle().equals(houseWork.getTitle()))
                        .count();

                // 한일 구하기
                long counted = houseWorkList.stream()
                        .filter(houseWorked -> houseWorked.getTitle().equals(houseWork.getTitle()) &&
                                houseWorked.getHouseWorkStatusType().equals(COMPLETED))
                        .count();

                if (counted != 0) { // 완료가 1개라도 있으면
                    double rate = (double) counted / (double) count * (double) 100;

                    houseWorkSuccessRateList.add(HouseWorkSuccessRate.builder()
                            .groupId(houseWork.getGroup().getZipHapGroupId())
                            .memberId(houseWork.getManager().getMemberId())
                            .houseWorkTitle(houseWork.getTitle())
                            .rate(rate)
                            .build());
                }
            }
        }

        return houseWorkSuccessRateList;
    }

    private List<ScoreToDifficulty> getDifficultyScore(List<HouseWork> houseWorkList) {
        List<ScoreToDifficulty> scoreToDifficultyList = new ArrayList<>();
        for (HouseWork houseWork : houseWorkList) {
            Optional<ScoreToDifficulty> optionalScoreToDifficulty = scoreToDifficultyList.stream()
                    .filter(scoreToDifficulty -> scoreToDifficulty.getHouseWorkTitle().equals(houseWork.getTitle()))
                    .findFirst();

            if (optionalScoreToDifficulty.isEmpty()) {
                scoreToDifficultyList.add(ScoreToDifficulty.builder()
                        .groupId(houseWork.getGroup().getZipHapGroupId())
                        .memberId(houseWork.getManager().getMemberId())
                        .houseWorkTitle(houseWork.getTitle())
                        .score(houseWork.getDifficulty().getScore())
                        .build());
            }
        }

        return scoreToDifficultyList;
    }

}
