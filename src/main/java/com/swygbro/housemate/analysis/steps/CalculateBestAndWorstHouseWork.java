package com.swygbro.housemate.analysis.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.analysis.message.best_worst.*;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.COMPLETED;
import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateBestAndWorstHouseWork { // TODO: HouseWorkId 를 넣기 위해 DTO 에 HouseWorkInfo 를 넣자

    private final AnalysisUtil analysisUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;
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

                    log.info("======= 반복에 따른 점수 구하기 ======= -> 이게 애매하다");
                    List<ScoreToRepetition> scoreToRepetitionList = getScoreToRepetitions(houseWorkList);

                    log.info("======= 성공률 구하기 =======");
                    List<HouseWorkSuccessRate> successRate = getSuccessRate(houseWorkList);

                    log.info("======= 합산 =======");
                    List<TotalGroupSum> totalGroupSumList = getTotalGroupSum(
                            difficultyScore,
                            scoreToRepetitionList,
                            successRate
                    );

                    log.info("======= DB 저장 =======");
                    for (TotalGroupSum totalGroupSum : totalGroupSumList) {
                        houseWorkAnalysisRepository.findByTodayAndGroupId(
                                now, totalGroupSum.getGroupId()
                        ).forEach(g -> g.setBestWorst(
                                null,
                                totalGroupSum.getBestInfo().getHouseWorkTitle(),
                                null,
                                null,
                                totalGroupSum.getWorstInfo().getHouseWorkTitle(),
                                null));
                    }

                    return FINISHED;
                }).build();
    }

    private List<TotalGroupSum> getTotalGroupSum(
            List<ScoreToDifficulty> difficultyScore,
            List<ScoreToRepetition> scoreToRepetitionList,
            List<HouseWorkSuccessRate> successRate
    ) { // 구하기
        List<SumScore> sumScoreList = new ArrayList<>();
        Map<String, List<SumScore>> groupingGroupId = sumScoreList.stream()
                .collect(Collectors.groupingBy(SumScore::getGroupId));

        List<TotalGroupSum> totalGroupSumList = new ArrayList<>();
        for (String groupId : groupingGroupId.keySet()) {
            List<SumScore> findByGroupId = groupingGroupId.get(groupId);

        }
        return totalGroupSumList;
    }

    private List<ScoreToRepetition> getScoreToRepetitions(List<HouseWork> houseWorkList) {
        List<ScoreToRepetition> scoreToRepetitionList = new ArrayList<>();
        for (HouseWork houseWork : houseWorkList) {
            Optional<ScoreToRepetition> optionalScoreToRepetition = scoreToRepetitionList.stream()
                    .filter(scoreToRepetition -> scoreToRepetition.getHouseWorkTitle().equals(houseWork.getTitle()))
                    .findFirst();

            if (optionalScoreToRepetition.isEmpty()) {
                // 세부 내용에 따라서 점수가 달라지는 것은 다음에 구현
                Integer score = houseWork.getCycle().getCycleType().getScore();

                scoreToRepetitionList.add(ScoreToRepetition.builder()
                        .groupId(houseWork.getGroup().getZipHapGroupId())
                        .memberId(houseWork.getManager().getMemberId())
                        .houseWorkTitle(houseWork.getTitle())
                        .score(score)
                        .build());
            }
        }
        return scoreToRepetitionList;
    }

    private List<HouseWorkSuccessRate> getSuccessRate(List<HouseWork> houseWorkList) {
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
