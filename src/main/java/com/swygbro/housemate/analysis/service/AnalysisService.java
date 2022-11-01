package com.swygbro.housemate.analysis.service;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.result.AnalysisComplete;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.exception.datanotfound.DataNotFoundType;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.그룹을_찾을_수_없습니다;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CurrentMemberUtil currentMemberUtil;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;

    public List<AnalysisComplete> execute() {
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        List<HouseWorkAnalysis> findByGroupId = houseWorkAnalysisRepository
                .findByGroupId(currentMemberANDGroupObject.getZipHapGroup().getZipHapGroupId());
        List<AnalysisComplete> analysisCompletes = new ArrayList<>();

        if (findByGroupId.isEmpty()) {
            return analysisCompletes;
        }

        for (HouseWorkAnalysis houseWorkAnalysis : findByGroupId) {
            MemberInfo memberInfo = modelMapper.map(currentMemberANDGroupObject, MemberInfo.class);

            Group group = groupRepository.findByLinkIdJoinFetchOwner(currentMemberANDGroupObject.getZipHapGroup().getLinkId())
                    .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

            MemberInfo owner = modelMapper.map(group.getOwner(), MemberInfo.class);

            GroupInfo groupInfo = GroupInfo.builder()
                    .zipHapGroupId(currentMemberANDGroupObject.getZipHapGroup().getZipHapGroupId())
                    .groupName(currentMemberANDGroupObject.getZipHapGroup().getGroupName())
                    .linkId(currentMemberANDGroupObject.getZipHapGroup().getLinkId())
                    .owner(owner)
                    .build();

            analysisCompletes.add(
                    AnalysisComplete.of(
                            houseWorkAnalysis.getAnalysisId(),
                            memberInfo,
                            groupInfo,
                            houseWorkAnalysis.getToday(),
                            houseWorkAnalysis.getShareRatioType(),
                            houseWorkAnalysis.getShareRatioPercent(),
                            houseWorkAnalysis.getMostTitle(),
                            houseWorkAnalysis.getCount(),
                            houseWorkAnalysis.getBestHouseWorkTitle(),
                            houseWorkAnalysis.getBestHouseWorkManager(),
                            houseWorkAnalysis.getWorstHouseWorkTitle(),
                            houseWorkAnalysis.getWorstHouseWorkManager(),
                            houseWorkAnalysis.getStartAt().toString(),
                            houseWorkAnalysis.getEndAt().toString()));
        }

        return analysisCompletes;
    }
}
