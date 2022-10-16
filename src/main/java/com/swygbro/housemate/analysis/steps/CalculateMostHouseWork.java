package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.mostmany.GroupFinalCount;
import com.swygbro.housemate.analysis.message.mostmany.MemberCountInfo;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.analysis.util.AnalysisUtil;
import com.swygbro.housemate.analysis.util.dto.AnalysisDto;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateMostHouseWork {
    private final AnalysisUtil analysisUtil;
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;
    private final MemberRepository memberRepository;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;

    @Bean
    @Transactional
    public Step executeByMember() {
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

                    log.info("======= memberId 으로 집안일 Count 하기 =======");
                    Map<String, List<AnalysisDto>> groupingMemberId = analysisDtoList.stream()
                            .collect(Collectors.groupingBy(AnalysisDto::getMemberId));
                    List<MemberCountInfo> groupCountInfos = getGroupCountInfos(groupingMemberId);

                    log.info("======= GroupId 중 집안일 많은 값 구하기 =======");
                    Map<String, List<MemberCountInfo>> groupByGroupId = groupCountInfos.stream()
                            .collect(Collectors.groupingBy(MemberCountInfo::getGroupId));
                    List<GroupFinalCount> extracted = extracted(groupByGroupId);

                    log.info("======= DB 저장 =======");
                    for (GroupFinalCount groupFinalCount : extracted) {
                        houseWorkAnalysisRepository.findByTodayAndGroupId(
                                now, groupFinalCount.getGroupId()
                        ).forEach(g -> g.setMostTitleAndCount(groupFinalCount.getTitle(), groupFinalCount.getCount()));
                    }
                    return FINISHED;
                }).build();
    }

    private List<GroupFinalCount> extracted(Map<String, List<MemberCountInfo>> groupByGroupId) {
        List<GroupFinalCount> groupFinalCountList = new ArrayList<>();

        for (String groupId : groupByGroupId.keySet()) {
            List<MemberCountInfo> findByGroupId = groupByGroupId.get(groupId);
            Map.Entry<String, Integer> maxEntry = null;
            String memberId = null;

            for (MemberCountInfo memberCountInfo : findByGroupId) {
                for (Map.Entry<String, Integer> entry : memberCountInfo.getTitleCount().entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        memberId = memberCountInfo.getMemberId();
                        maxEntry = entry;
                    }
                }
            }

            groupFinalCountList.add(GroupFinalCount.of(memberId, groupId, maxEntry.getKey(), maxEntry.getValue()));
        }

        return groupFinalCountList;
    }

    private List<MemberCountInfo> getGroupCountInfos(Map<String, List<AnalysisDto>> groupingMemberId) {
        List<MemberCountInfo> memberCountInfos = new ArrayList<>();

        for (String memberId : groupingMemberId.keySet()) {
            List<AnalysisDto> findByMemberId = groupingMemberId.get(memberId);

            int count = 1;
            Map<String, Integer> integerMap = new HashMap<>();
            for (AnalysisDto mostManyInfo : findByMemberId) {
                if (integerMap.get(mostManyInfo.getTitle()) == null) {
                    integerMap.put(mostManyInfo.getTitle(), count);
                } else {
                    count++;
                    integerMap.put(mostManyInfo.getTitle(), count);
                }
            }

            Member member = memberRepository.findByIdJoinFetchGroup(memberId)
                    .orElseThrow(null);
            memberCountInfos.add(MemberCountInfo.of(member.getZipHapGroup().getZipHapGroupId(), memberId, integerMap));

        }
        return memberCountInfos;
    }
}
