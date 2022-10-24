package com.swygbro.housemate.analysis.service;

import com.swygbro.housemate.analysis.domain.HouseWorkAnalysis;
import com.swygbro.housemate.analysis.message.result.AnalysisComplete;
import com.swygbro.housemate.analysis.repository.HouseWorkAnalysisRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CurrentMemberUtil currentMemberUtil;
    private final HouseWorkAnalysisRepository houseWorkAnalysisRepository;

    public List<AnalysisComplete> execute() {
        Member currentMemberANDGroupObject = currentMemberUtil.getCurrentMemberANDGroupObject();
        List<HouseWorkAnalysis> findByGroupId = houseWorkAnalysisRepository
                .findByGroupId(currentMemberANDGroupObject.getZipHapGroup().getZipHapGroupId());

        List<AnalysisComplete> analysisCompletes = new ArrayList<>();
        for (HouseWorkAnalysis houseWorkAnalysis : findByGroupId) {
            System.out.println("houseWorkAnalysis = " + houseWorkAnalysis);
        }

        return analysisCompletes;
    }
}
