package com.swygbro.housemate.housework.service;


import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import com.swygbro.housemate.housework.message.SearchHouseWorkAtDate;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchDateProcess {

    private final HouseWorkRepository houseWorkRepository;
    private final GroupRepository groupRepository;
    private final CurrentMemberUtil currentMemberUtil;

    public HouseWorkByMember executeByGroup(SearchHouseWorkAtDate searchHouseWorkAtDate) {
        Group zipHapGroup = currentMemberUtil.getCurrentMemberANDGroupObject().getZipHapGroup();
        Group byLinkIdQuery = groupRepository.findByLinkIdJoinFetchOwner(zipHapGroup.getLinkId()).orElseThrow(null);
        return houseWorkRepository.searchHouseWorkAtDateByGroup(
                searchHouseWorkAtDate.getStartAt(),
                searchHouseWorkAtDate.getEndAt(), byLinkIdQuery
        );
    }

}
