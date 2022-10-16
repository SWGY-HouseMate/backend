package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.ratio.MemberFinalShareRatio;
import com.swygbro.housemate.analysis.message.ratio.MemberPercentInfo;
import com.swygbro.housemate.analysis.message.ratio.ShareRatioInfo;
import com.swygbro.housemate.analysis.message.ratio.ShareRatioType;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.swygbro.housemate.analysis.message.ratio.ShareRatioType.*;
import static org.springframework.batch.core.ExitStatus.FAILED;
import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateShareRatio {

    private final AnalysisUtil analysisUtil;
    private final UUIDUtil uuidUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;

    @Bean
    @Transactional
    public Step executeByGroup() {
        return stepBuilderFactory.get("CalculateShareRatioByGroupStep")
                .tasklet((contribution, chunkContext) -> {

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.validation(
                            houseWorkRepository.searchHouseWorkByToday(now)
                    );

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

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

                    log.info("======= DB =======");
                    for (MemberFinalShareRatio memberFinalShareRatio : memberFinalShareRatios) {
                        Optional<HouseWorkAnalysis> byToday = houseWorkAnalysisRepository.findByTodayAndMemberIdAndGroupId(now, memberFinalShareRatio.getMemberId(), memberFinalShareRatio.getGroupId());

                        if (byToday.isEmpty()) {
                            houseWorkAnalysisRepository.save(HouseWorkAnalysis.builder()
                                    .analysisId(uuidUtil.create())
                                    .memberId(memberFinalShareRatio.getMemberId())
                                    .groupId(memberFinalShareRatio.getGroupId())
                                    .today(now)
                                    .shareRatioType(memberFinalShareRatio.getShareRatioType())
                                    .shareRatioPercent(memberFinalShareRatio.getPercent())
                                    .build());

                            return FINISHED;
                        }

                        byToday.get().setShareRatioTypeAndShareRatioPercent(memberFinalShareRatio.getShareRatioType(), memberFinalShareRatio.getPercent());
                        return FINISHED;
                    }

                    return FINISHED;
                })
                .build();
    }

    private List<MemberFinalShareRatio> getMemberFinalShareRatios(List<MemberPercentInfo> memberPercentInfoList) {
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

    private List<MemberPercentInfo> getMemberPercentInfos(Map<String, List<ShareRatioInfo>> groupInfoListMap) {
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

    private MemberFinalShareRatio getMemberFinalShareRatio(MemberPercentInfo memberPercentInfo, double percent, ShareRatioType shareRatioType) {
        return MemberFinalShareRatio.of(memberPercentInfo.getGroupId(),
                memberPercentInfo.getMemberId(),
                percent,
                shareRatioType);
    }

}
