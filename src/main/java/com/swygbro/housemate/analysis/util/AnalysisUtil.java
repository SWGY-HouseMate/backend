package com.swygbro.housemate.analysis.util;

import com.swygbro.housemate.analysis.util.dto.AnalysisDto;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalysisUtil {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // 그룹에 멤버가 1명밖에 없으면 제거
    public List<HouseWork> removeOnlyOneMemberInTheGroup(List<HouseWork> inputList) {
        for (HouseWork houseWork : inputList) {
            Group zipHapGroup = houseWork.getManager().getZipHapGroup();
            List<Member> byZipHapGroup = memberRepository.findByZipHapGroup(zipHapGroup);

            if (byZipHapGroup.size() != 2) {
                inputList.remove(houseWork);
            }
        }

        return inputList;
    }

    public List<AnalysisDto> converterHouseWorkDto(List<HouseWork> houseWorkList) {
        List<AnalysisDto> analysisDtoList = new ArrayList<>();
        for (HouseWork houseWork : houseWorkList) {
            analysisDtoList.add(AnalysisDto.of(
                    houseWork.getHouseWorkId(),
                    houseWork.getGroup().getZipHapGroupId(),
                    houseWork.getManager().getMemberId(),
                    houseWork.getTitle(),
                    houseWork.getHouseWorkStatusType(),
                    houseWork.getDifficulty(),
                    houseWork.getToday(),
                    houseWork.getIsCycle(),
                    houseWork.getIsCycle() ? houseWork.getCycle().getCycleId() : null
            ));
        }

        return analysisDtoList;
    }
}
