package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.ratio.MemberFinalShareRatio;
import com.swygbro.housemate.analysis.message.ratio.MemberPercentInfo;
import com.swygbro.housemate.analysis.message.ratio.ShareRatioInfo;
import com.swygbro.housemate.analysis.message.ratio.ShareRatioType;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.analysis.util.dto.AnalysisDto;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.uuid.UUIDUtil;
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

import static com.swygbro.housemate.analysis.message.ratio.ShareRatioType.*;
import static java.time.LocalDateTime.now;
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
    public Step executeByGroup() { // 집안일이 둘중에 한명만 있어도 처리해야한다.
        return stepBuilderFactory.get("CalculateShareRatioByGroupStep")
                .tasklet((contribution, chunkContext) -> {

                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = analysisUtil.removeOnlyOneMemberInTheGroup(
                            houseWorkRepository.searchHouseWorkByToday(now)
                    );

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    log.info("======= HouseWork DTO 로 변환 =======");
                    List<AnalysisDto> analysisDtoList = analysisUtil.converterHouseWorkDto(houseWorkList);

                    log.info("======= MemberId 별 난위도 Score 구하기 =======");
                    List<ShareRatioInfo> shareRatioInfos = memberIdByScore(now, analysisDtoList);

                    log.info("======= Group Member 별 백분율 구하기 =======");
                    Map<String, List<ShareRatioInfo>> groupInfoListMap = shareRatioInfos.stream()
                            .collect(Collectors.groupingBy(ShareRatioInfo::getGroupId));
                    List<MemberPercentInfo> memberPercentInfoList = getMemberPercentInfos(groupInfoListMap);

                    log.info("======= Group Member 별 비율 구하기 =======");
                    List<MemberFinalShareRatio> memberFinalShareRatios = getMemberFinalShareRatios(memberPercentInfoList);

                    log.info("======= DB 저장 =======");
                    for (MemberFinalShareRatio memberFinalShareRatio : memberFinalShareRatios) {
                        System.out.println("memberFinalShareRatio = " + memberFinalShareRatio);
                    }


                    for (MemberFinalShareRatio memberFinalShareRatio : memberFinalShareRatios) {
                        Optional<HouseWorkAnalysis> findByMemberIdAndToday = houseWorkAnalysisRepository.findByTodayAndMemberId(
                                now,
                                memberFinalShareRatio.getMemberId()
                        );

                        if (findByMemberIdAndToday.isEmpty()) {
                            houseWorkAnalysisRepository.save(HouseWorkAnalysis.builder()
                                    .analysisId(uuidUtil.create())
                                    .memberId(memberFinalShareRatio.getMemberId())
                                    .groupId(memberFinalShareRatio.getGroupId())
                                    .today(now)
                                    .shareRatioType(memberFinalShareRatio.getShareRatioType())
                                    .shareRatioPercent(memberFinalShareRatio.getPercent())
                                    .startAt(now())
                                    .build());
                        } else {
                            findByMemberIdAndToday.get().setShareRatioTypeAndShareRatioPercent(
                                    memberFinalShareRatio.getShareRatioType(),
                                    memberFinalShareRatio.getPercent()
                            );
                        }
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
            } else if(percent >= 40.0 && percent < 60.0) {
                memberFinalShareRatios.add(getMemberFinalShareRatio(memberPercentInfo, percent, 잘하고_있어요));
            } else if(percent >= 60.0 && percent < 100.0) {
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

    private List<ShareRatioInfo> memberIdByScore(LocalDate now, List<AnalysisDto> analysisDtoList) {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        for (AnalysisDto analysisDto: analysisDtoList) {
            String memberId = analysisDto.getMemberId();
            Integer difficultyTypeScore = analysisDto.getDifficulty().getScore();

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

            shareRatioInfos.add(ShareRatioInfo.of(member.getZipHapGroup().getZipHapGroupId(), memberId, (houseWorkCount + score)));
        }

        return shareRatioInfos;
    }

    private MemberFinalShareRatio getMemberFinalShareRatio(MemberPercentInfo memberPercentInfo, double percent, ShareRatioType shareRatioType) {
        return MemberFinalShareRatio.of(memberPercentInfo.getGroupId(),
                memberPercentInfo.getMemberId(),
                shareRatioType,
                percent);
    }

}
