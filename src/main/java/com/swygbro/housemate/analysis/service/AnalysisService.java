package com.swygbro.housemate.analysis.service;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.result.AnalysisComplete;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CurrentMemberUtil currentMemberUtil;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;
    private final ModelMapper modelMapper;

    public List<AnalysisComplete> execute() {
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        List<HouseWorkAnalysis> findByGroupId = houseWorkAnalysisRepository
                .findByGroupId(currentMemberANDGroupObject.getZipHapGroup().getZipHapGroupId());

        List<AnalysisComplete> analysisCompletes = new ArrayList<>();
        for (HouseWorkAnalysis houseWorkAnalysis : findByGroupId) {
            MemberInfo memberInfo = modelMapper.map(currentMemberANDGroupObject, MemberInfo.class);
            GroupInfo groupInfo = modelMapper.map(currentMemberANDGroupObject.getZipHapGroup(), GroupInfo.class);

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
