package com.swygbro.housemate.housework.service;


import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.*;
import com.swygbro.housemate.housework.repository.cycle.CycleRepository;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.*;

@Service
@RequiredArgsConstructor
public class SearchDateProcess {

    private final HouseWorkRepository houseWorkRepository;
    private final GroupRepository groupRepository;
    private final CurrentMemberUtil currentMemberUtil;
    private final ModelMapper modelMapper;
    private final CycleRepository cycleRepository;

    public HouseWorkByMember executeByGroup(SearchHouseWorkAtDate searchHouseWorkAtDate) {
        Group zipHapGroup = currentMemberUtil.getCurrentMemberANDGroupObject().getZipHapGroup();
        Group byLinkIdQuery = groupRepository.findByLinkIdJoinFetchOwner(zipHapGroup.getLinkId())
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));
        return houseWorkRepository.searchHouseWorkAtDateByGroup(
                searchHouseWorkAtDate.getStartAt(),
                searchHouseWorkAtDate.getEndAt(), byLinkIdQuery
        );
    }

    public WorkById searchById(String houseWorkId) {
        HouseWork houseWork = houseWorkRepository.searchHouseWorkIdJoinManger(houseWorkId)
                .orElseThrow(() -> new DataNotFoundException(집안일을_찾을_수_없습니다));
        MemberInfo memberInfo = modelMapper.map(houseWork.getManager(), MemberInfo.class);
        GroupInfo groupInfo = modelMapper.map(houseWork.getGroup(), GroupInfo.class);

        CycleInfo cycleInfo = null;
        if (houseWork.getIsCycle()) {
            Cycle cycle = cycleRepository.findByCycleId(houseWork.getCycle().getCycleId())
                    .orElseThrow(() -> new DataNotFoundException(반복_주기를_찾을_수_없습니다));

            cycleInfo = CycleInfo.builder()
                    .cycleId(cycle.getCycleId())
                    .cycleType(cycle.getCycleType())
                    .props(cycle.getProps())
                    .startAt(cycle.getStartAt())
                    .endAt(cycle.getEndAt())
                    .build();
        }

        return WorkById.builder()
                .houseWorkId(houseWorkId)
                .title(houseWork.getTitle())
                .difficulty(houseWork.getDifficulty())
                .memberInfo(memberInfo)
                .today(houseWork.getToday().toString())
                .houseWorkStatusType(houseWork.getHouseWorkStatusType())
                .groupInfo(groupInfo)
                .isCycle(houseWork.getIsCycle())
                .cycleInfo(cycleInfo)
                .build();
    }
}
