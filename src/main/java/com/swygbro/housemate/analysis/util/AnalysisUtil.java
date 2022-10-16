package com.swygbro.housemate.analysis.util;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalysisUtil {
    private final MemberRepository memberRepository;


    public List<HouseWork> validation(List<HouseWork> inputHouseWorkList) {
        return removeOnlyOneMemberInTheGroup(removeOnlyOneMemberInTheGroup(inputHouseWorkList));
    }

    // 그룹에 멤버가 1명밖에 없으면 제거
    private List<HouseWork> removeOnlyOneMemberInTheGroup(List<HouseWork> inputList) {
        for (HouseWork houseWork : inputList) {
            Group zipHapGroup = houseWork.getManager().getZipHapGroup();
            List<Member> byZipHapGroup = memberRepository.findByZipHapGroup(zipHapGroup);

            if (byZipHapGroup.size() != 2) {
                inputList.remove(houseWork);
            }
        }

        return inputList;
    }

}
