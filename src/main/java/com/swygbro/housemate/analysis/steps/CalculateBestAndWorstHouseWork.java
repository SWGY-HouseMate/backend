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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.COMPLETED;
import static java.time.LocalDateTime.now;
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
    @Transactional
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
                                totalGroupSum.getBestInfo().getHouseWorkTitle(),
                                totalGroupSum.getBestInfo().getHouseWorkManager(),
                                totalGroupSum.getWorstInfo().getHouseWorkTitle(),
                                totalGroupSum.getWorstInfo().getHouseWorkManager(),
                                now()));
                    }

                    return FINISHED;
                }).build();
    }

    private List<TotalGroupSum> getTotalGroupSum(
            List<ScoreToDifficulty> difficultyScore,
            List<ScoreToRepetition> scoreToRepetitionList,
            List<HouseWorkSuccessRate> successRate
    ) {
        if (difficultyScore.size() == 0) {
            throw new IllegalStateException("계산된 데이터가 없습니다.");
        }

        List<SumScore> sumScoreList = new ArrayList<>();
        for (ScoreToDifficulty scoreToDifficulty : difficultyScore) {
            ScoreToRepetition scoreToRepetition = scoreToRepetitionList.stream()
                    .filter(s -> s.getGroupId().equals(scoreToDifficulty.getGroupId()))
                    .collect(Collectors.toList())
                    .get(0);

            HouseWorkSuccessRate houseWorkSuccessRate = successRate.stream()
                    .filter(s -> s.getGroupId().equals(scoreToDifficulty.getGroupId()))
                    .collect(Collectors.toList())
                    .get(0);

            // 성공율 / 주기(일수) * 난이도 배점
            double sum = houseWorkSuccessRate.getRate() / (double) scoreToRepetition.getScore() * (double) scoreToDifficulty.getScore();
            sumScoreList.add(
                    SumScore.builder()
                            .groupId(scoreToDifficulty.getGroupId())
                            .memberId(scoreToDifficulty.getMemberId())
                            .sum(sum)
                            .houseWorkTitle(scoreToDifficulty.getHouseWorkTitle())
                            .build()
            );
        }

        // 그룹 별로 가장 적은 값이랑 가장 큰 값을 고름
        List<TotalGroupSum> totalGroupSumList = new ArrayList<>();
        Map<String, List<SumScore>> groupByGroup = sumScoreList.stream()
                .collect(Collectors.groupingBy(SumScore::getGroupId));
        for (String groupId : groupByGroup.keySet()) {
            List<SumScore> sumScores = groupByGroup.get(groupId);

            double max = sumScores.stream().mapToDouble(SumScore::getSum)
                    .max()
                    .orElseThrow(NoSuchElementException::new);

            double min = sumScores.stream().mapToDouble(SumScore::getSum)
                    .min()
                    .orElseThrow(NoSuchElementException::new);

            SumScore maxSumScore = sumScores.stream()
                    .filter(s -> s.getSum().equals(max))
                    .collect(Collectors.toList())
                    .get(0);

            SumScore minSumScore = sumScores.stream()
                    .filter(s -> s.getSum().equals(min))
                    .collect(Collectors.toList())
                    .get(0);

            totalGroupSumList.add(
              TotalGroupSum.builder()
                      .groupId(groupId)
                      .bestInfo(BestInfo.of(maxSumScore.getHouseWorkTitle(), maxSumScore.getMemberId()))
                      .worstInfo(WorstInfo.of(minSumScore.getHouseWorkTitle(), minSumScore.getMemberId()))
                      .build()
            );
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
