package com.swygbro.housemate.housework.service;


import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CycleInfo;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.message.HouseWorkInfo;
import com.swygbro.housemate.housework.message.SearchHouseWorkAtDate;
import com.swygbro.housemate.housework.repository.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDateProcess {

    private final HouseWorkRepository houseWorkRepository;

    private final MemberRepository memberRepository;
    private final CurrentMemberUtil currentMemberUtil;

    private final ModelMapper mapper;

    public HoseWorkRes executeByGroup(SearchHouseWorkAtDate searchHouseWorkAtDate) {
        Member currentMemberObject = currentMemberUtil.getCurrentMemberObject();

        Member memberEmailJPQL = memberRepository.findByMemberEmailJPQL(currentMemberObject.getMemberEmail());
        List<HouseWork> houseWorkList = houseWorkRepository.searchHouseWorkAtDateByGroup(searchHouseWorkAtDate.getStartAt(),
                    searchHouseWorkAtDate.getEndAt(), memberEmailJPQL.getZipHapGroup());

        List<HouseWorkInfo> houseWorkInfos = new ArrayList<>();
        for (HouseWork houseWork : houseWorkList) {
            MemberInfo memberInfo = mapper.map(houseWork.getManager(), MemberInfo.class);
            CycleInfo cycleInfo = mapper.map(houseWork.getCycle(), CycleInfo.class);

            houseWorkInfos.add(HouseWorkInfo.of(
                    houseWork.getHouseWorkId(),
                    houseWork.getTitle(),
                    houseWork.getDifficulty(),
                    houseWork.getToday(),
                    houseWork.getIsCompleted(),
                    houseWork.getIsCycle(),
                    cycleInfo, memberInfo));
        }

        GroupInfo groupInfo = mapper.map(houseWorkList.get(0).getGroup(), GroupInfo.class);
        return HoseWorkRes.of(houseWorkInfos, groupInfo);
    }

}
