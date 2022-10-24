package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.message.HouseWorkCountForGroup;
import com.swygbro.housemate.housework.message.SearchHouseWorkAtDate;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseWorkCountProcess {

    private final HouseWorkRepository houseWorkRepository;
    private final CurrentMemberUtil currentMemberUtil;

    public HouseWorkCountForGroup executeByGroup(SearchHouseWorkAtDate searchHouseWorkAtDate) {
        Group zipHapGroup = currentMemberUtil.getCurrentMemberANDGroupObject().getZipHapGroup();
        return houseWorkRepository.searchHouseWorkCountByMember(
                searchHouseWorkAtDate.getStartAt(),
                searchHouseWorkAtDate.getEndAt(),
                zipHapGroup
        );
    }
}
