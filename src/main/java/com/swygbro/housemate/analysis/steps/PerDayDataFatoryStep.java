package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.ShareRatioInfo;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PerDayDataFatoryStep {
    private final StepBuilderFactory stepBuilderFactory;
    private final ModelMapper modelMapper;
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;

    @Bean
    public Step execute() {
        return stepBuilderFactory.get("GetPerDayData")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======= 필요한 집안일 하루 데이터 가져오기 =======");
                    Map<String, Integer> stringIntegerMap = new HashMap<>();

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = houseWorkRepository.searchHouseWorkByToday(now);
                    for (HouseWork houseWork: houseWorkList) {
                        String memberId = houseWork.getManager().getMemberId();
                        Integer difficultyTypeScore = houseWork.getDifficulty().getScore();

                        Integer findByMemberScore = stringIntegerMap.get(memberId);
                        if (findByMemberScore != null) {
                            difficultyTypeScore += findByMemberScore;
                            stringIntegerMap.put(memberId, difficultyTypeScore);
                        } else {
                            stringIntegerMap.put(memberId, difficultyTypeScore);
                        }
                    }

                    List<ShareRatioInfo> shareRatioInfos = new ArrayList<>();
                    for (String memberId : stringIntegerMap.keySet()) {
                        Member member = memberRepository.findByIdJoinFetchGroup(memberId).orElseThrow(null);
                        Integer houseWorkCount = houseWorkRepository.countByMember(now, member).intValue();
                        Integer score = stringIntegerMap.get(memberId);

                        shareRatioInfos.add(ShareRatioInfo.of(memberId, (houseWorkCount + score), member.getZipHapGroup().getZipHapGroupId()));
                    }

                    // 그룹이 같은 것끼리 백분율 구하기
                    Map<String, List<ShareRatioInfo>> groupInfoListMap = shareRatioInfos.stream()
                            .collect(Collectors.groupingBy(ShareRatioInfo::getGroupId));
                    for (String groupId : groupInfoListMap.keySet()) {
                        List<ShareRatioInfo> shareRatioInfoList = groupInfoListMap.get(groupId);

                        int groupSum = shareRatioInfoList.stream()
                                .mapToInt(ShareRatioInfo::getSum).sum();

                        shareRatioInfoList.forEach(s -> {
                            double percent = (double) s.getSum() / (double) groupSum * 100.0;

                            if (percent >= 10.0 && percent < 20.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 너무 많습니다.");
                            } else if (percent >= 20.0 && percent < 30.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 너무 많습니다.");
                            } else if (percent >= 30.0 && percent < 40.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 너무 많습니다.");
                            } else if (percent >= 40.0 && percent < 50.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 조금 많습니다.");
                            } else if (percent >= 50.0 && percent < 60.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 조금 많습니다.");
                            } else if (percent >= 60.0 && percent < 70.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 조금 많습니다.");
                            } else if (percent >= 70.0 && percent < 80.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 적당합니다.");
                            } else if (percent >= 80.0 && percent < 90.0) {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 적당합니다.");
                            } else {
                                System.out.println("memberId : " + s.getMemberId() + "님은 " + percent + "% 로 적당합니다.");
                            }
                        });
                    }

                    // 비율 구하기




                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
