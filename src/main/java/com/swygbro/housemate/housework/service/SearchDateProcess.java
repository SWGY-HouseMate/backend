package com.swygbro.housemate.housework.service;


import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CycleInfo;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import com.swygbro.housemate.housework.message.SearchHouseWorkAtDate;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
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
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final CurrentMemberUtil currentMemberUtil;
    private final ModelMapper mapper;

//    public HoseWorkRes executeByGroup(SearchHouseWorkAtDate searchHouseWorkAtDate) {
//        Member currentMemberObject = currentMemberUtil.getCurrentMemberObject();
//
//        Member memberEmailJPQL = memberRepository.findByMemberEmailJPQL(currentMemberObject.getMemberEmail());
//        Group byLinkIdQuery = groupRepository.findByLinkIdQuery(memberEmailJPQL.getZipHapGroup().getLinkId()).orElseThrow(null);
//        List<HouseWork> houseWorkList = houseWorkRepository.searchHouseWorkAtDateByGroupDSL(searchHouseWorkAtDate.getStartAt(),
//                    searchHouseWorkAtDate.getEndAt(), memberEmailJPQL.getZipHapGroup());
//
//        List<HouseWorkByMember> houseWorkByMembers = new ArrayList<>();
//        for (HouseWork houseWork : houseWorkList) {
//            MemberInfo memberInfo = mapper.map(houseWork.getManager(), MemberInfo.class);
//            CycleInfo cycleInfo = mapper.map(houseWork.getCycle(), CycleInfo.class);
//
//            houseWorkByMembers.add(HouseWorkByMember.of(
//                    houseWork.getHouseWorkId(),
//                    houseWork.getTitle(),
//                    houseWork.getDifficulty(),
//                    houseWork.getToday(),
//                    houseWork.getIsCompleted(),
//                    houseWork.getIsCycle(),
//                    cycleInfo, memberInfo));
//        }
//
//        GroupInfo groupInfo = mapper.map(byLinkIdQuery, GroupInfo.class);
//        return HoseWorkRes.of(houseWorkByMembers, groupInfo);
//    }


    public HouseWorkByMember executeByGroup2(SearchHouseWorkAtDate searchHouseWorkAtDate) {
        // 현재 그룹을 구해야 해야한다.
        Group zipHapGroup = currentMemberUtil.getCurrentMemberANDGroupObject().getZipHapGroup();
        Group byLinkIdQuery = groupRepository.findByLinkIdQuery(zipHapGroup.getLinkId()).orElseThrow(null);
        HouseWorkByMember houseWorkByMember = houseWorkRepository.searchHouseWorkAtDateByGroupDSL(
                searchHouseWorkAtDate.getStartAt(),
                searchHouseWorkAtDate.getEndAt(), byLinkIdQuery
        );
        return houseWorkByMember;
    }
}
