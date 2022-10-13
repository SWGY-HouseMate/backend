package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.MemberFinalShareRatio;
import com.swygbro.housemate.analysis.message.MemberPercentInfo;
import com.swygbro.housemate.analysis.message.ShareRatioInfo;
import com.swygbro.housemate.analysis.message.ShareRatioType;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.swygbro.housemate.analysis.message.ShareRatioType.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PerDayDataFatoryStep {
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;

    @Bean
    public Step execute() {
        return stepBuilderFactory.get("GetPerDayData")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======= 필요한 집안일 하루 데이터 가져오기 =======");
                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = houseWorkRepository.searchHouseWorkByToday(now);

                    log.info("======= 난위도에 맞는 Score 구하기 =======");
                    Map<String, Integer> stringIntegerMap = extracted(houseWorkList);

                    log.info("======= Member 별로 Score 구하기 =======");
                    List<ShareRatioInfo> shareRatioInfos = getShareRatioInfos(now, stringIntegerMap);

                    log.info("======= 그룹이 같은 것끼리 백분율 구하기 =======");
                    Map<String, List<ShareRatioInfo>> groupInfoListMap = shareRatioInfos.stream()
                            .collect(Collectors.groupingBy(ShareRatioInfo::getGroupId));
                    List<MemberPercentInfo> memberPercentInfoList = getMemberPercentInfos(groupInfoListMap);

                    log.info("======= 비율 구하기 =======");
                    List<MemberFinalShareRatio> memberFinalShareRatios = getMemberFinalShareRatios(memberPercentInfoList);

                    log.info("======= 테스트 =======");
                    for (MemberFinalShareRatio memberFinalShareRatio : memberFinalShareRatios) {
                        System.out.println("memberId : " + memberFinalShareRatio.getMemberId() + " type : " + memberFinalShareRatio.getShareRatioType() + "(" + memberFinalShareRatio.getPercent() +"%)");
                    }

                    log.info("======= 마지막 그룹에 대한 비율 구하기 =======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private static List<MemberFinalShareRatio> getMemberFinalShareRatios(List<MemberPercentInfo> memberPercentInfoList) {
        List<MemberFinalShareRatio> memberFinalShareRatios = new ArrayList<>();
        for (MemberPercentInfo memberPercentInfo : memberPercentInfoList) {
            double percent = memberPercentInfo.getPercent();

            if (percent >= 0.0 && percent < 40.0) {
                memberFinalShareRatios.add(getMemberFinalShareRatio(memberPercentInfo, percent, 너무_적어요));
            } else if(percent >= 40.0 && percent < 70.0) {
                memberFinalShareRatios.add(getMemberFinalShareRatio(memberPercentInfo, percent, 잘하고_있어요));
            } else if(percent >= 70.0 && percent < 100.0) {
                memberFinalShareRatios.add(getMemberFinalShareRatio(memberPercentInfo, percent, 너무_많아요));
            }
        }

        return memberFinalShareRatios;
    }

    private static List<MemberPercentInfo> getMemberPercentInfos(Map<String, List<ShareRatioInfo>> groupInfoListMap) {
        List<MemberPercentInfo> memberPercentInfoList = new ArrayList<>();
        for (String groupId : groupInfoListMap.keySet()) {
            List<ShareRatioInfo> shareRatioInfoList = groupInfoListMap.get(groupId);

            int groupSum = shareRatioInfoList.stream()
                    .mapToInt(ShareRatioInfo::getSum).sum();

            shareRatioInfoList.forEach(s -> {
                double percent = (double) s.getSum() / (double) groupSum * 100.0;
                memberPercentInfoList.add(MemberPercentInfo.of(groupId, s.getMemberId(), percent));
            });
        }

        return memberPercentInfoList;
    }

    private List<ShareRatioInfo> getShareRatioInfos(LocalDate now, Map<String, Integer> stringIntegerMap) {
        List<ShareRatioInfo> shareRatioInfos = new ArrayList<>();
        for (String memberId : stringIntegerMap.keySet()) {
            Member member = memberRepository.findByIdJoinFetchGroup(memberId).orElseThrow(null);
            Integer houseWorkCount = houseWorkRepository.countByMember(now, member).intValue();
            Integer score = stringIntegerMap.get(memberId);

            shareRatioInfos.add(ShareRatioInfo.of(memberId, (houseWorkCount + score), member.getZipHapGroup().getZipHapGroupId()));
        }

        return shareRatioInfos;
    }

    private Map<String, Integer> extracted(List<HouseWork> houseWorkList) {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
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

        return stringIntegerMap;
    }

    private static MemberFinalShareRatio getMemberFinalShareRatio(MemberPercentInfo memberPercentInfo, double percent, ShareRatioType shareRatioType) {
        return MemberFinalShareRatio.of(memberPercentInfo.getGroupId(),
                memberPercentInfo.getMemberId(),
                percent,
                shareRatioType);
    }

}
