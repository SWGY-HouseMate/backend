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
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.그룹을_찾을_수_없습니다;
import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CurrentMemberUtil currentMemberUtil;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;
    private final ModelMapper modelMapper;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    public List<AnalysisComplete> execute(LocalDate today) {
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();

        List<HouseWorkAnalysis> findByGroupId = houseWorkAnalysisRepository.findByGroupIdAndToday(
                currentMemberANDGroupObject.getZipHapGroup().getZipHapGroupId(),
                today.minusDays(1)
        );

        List<AnalysisComplete> analysisCompletes = new ArrayList<>();
        if (findByGroupId.isEmpty()) {
            return analysisCompletes;
        }

        for (HouseWorkAnalysis houseWorkAnalysis : findByGroupId) {
            Member member = memberRepository.findByIdJoinFetchGroup(houseWorkAnalysis.getMemberId())
                    .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
            MemberInfo memberInfo = modelMapper.map(member, MemberInfo.class);

            Group group = groupRepository.findByLinkIdJoinFetchOwner(member.getZipHapGroup().getLinkId())
                    .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

            MemberInfo owner = modelMapper.map(group.getOwner(), MemberInfo.class);

            GroupInfo groupInfo = GroupInfo.builder()
                    .zipHapGroupId(group.getZipHapGroupId())
                    .groupName(group.getGroupName())
                    .linkId(group.getLinkId())
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
