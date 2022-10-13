package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.message.mostmany.GroupCountInfo;
import com.swygbro.housemate.analysis.message.mostmany.GroupFinalCount;
import com.swygbro.housemate.analysis.message.mostmany.MostManyInfo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CalculateMostHouseWork {
    private final StepBuilderFactory stepBuilderFactory;
    private final HouseWorkRepository houseWorkRepository;

    @Bean
    public Step executeByMember() {
        return stepBuilderFactory.get("CalculateShareRatioByGroupStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======= 필요한 집안일 가져오기 =======");
                    LocalDate now = LocalDate.now();
                    List<HouseWork> houseWorkList = houseWorkRepository.searchCalculateMostHouseWork(now);

                    if (houseWorkList.isEmpty()) {
                        return FINISHED;
                    }

                    log.info("======= 보기 좋은 형식으로 컨버팅 =======");
                    List<MostManyInfo> mostManyInfos = getMostManyInfos(houseWorkList);

                    log.info("======= group 을 기준으로 집안일 Count 하기 =======");
                    List<GroupCountInfo> groupCountInfos = getGroupCountInfos(mostManyInfos);

                    log.info("======= 제일 집안일이 많은 값 구하기 =======");
                    GroupFinalCount extracted = extracted(groupCountInfos);

                    log.info("======= 테스트 =======");
                    System.out.println("extracted = " + extracted);

                    log.info("======= DB 업데이트 =======");

                    return FINISHED;
                }).build();
    }

    private GroupFinalCount extracted(List<GroupCountInfo> groupCountInfos) {
        Map.Entry<String, Integer> maxEntry = null;
        String groupId = null;
        for (GroupCountInfo groupCountInfo : groupCountInfos) {
            groupId = groupCountInfo.getGroupId();
            for (Map.Entry<String, Integer> entry : groupCountInfo.getTitleCount().entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                {
                    maxEntry = entry;
                }
            }
        }
        return GroupFinalCount.of(groupId, maxEntry.getKey(), maxEntry.getValue());
    }

    private List<GroupCountInfo> getGroupCountInfos(List<MostManyInfo> mostManyInfos) {
        List<GroupCountInfo> groupCountInfos = new ArrayList<>();
        Map<String, List<MostManyInfo>> groupingGroupId = mostManyInfos.stream()
                .collect(Collectors.groupingBy(MostManyInfo::getGroupId));
        for (String groupId : groupingGroupId.keySet()) {
            List<MostManyInfo> findByGroupId = groupingGroupId.get(groupId);

            int count = 1;
            Map<String, Integer> integerMap = new HashMap<>();
            for (MostManyInfo mostManyInfo : findByGroupId) {
                if (integerMap.get(mostManyInfo.getTitle()) == null) {
                    integerMap.put(mostManyInfo.getTitle(), count);
                } else {
                    count++;
                    integerMap.put(mostManyInfo.getTitle(), count);
                }
            }

            groupCountInfos.add(GroupCountInfo.of(groupId, integerMap));
        }
        return groupCountInfos;
    }

    private List<MostManyInfo> getMostManyInfos(List<HouseWork> houseWorkList) {
        List<MostManyInfo> mostManyInfos = new ArrayList<>();
        houseWorkList.forEach(houseWork -> mostManyInfos.add(MostManyInfo.of(
                houseWork.getGroup().getZipHapGroupId(),
                houseWork.getManager().getMemberId(),
                houseWork.getTitle(),
                true)
        ));
        return mostManyInfos;
    }

}
