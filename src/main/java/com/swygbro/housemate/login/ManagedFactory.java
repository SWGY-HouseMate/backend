package com.swygbro.housemate.login;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ManagedFactory {

    private final CurrentMemberUtil currentMemberUtil;

    private final MemberRepository memberRepository;

    @Transactional
    public void assign(List<HouseWork> houseWorkList) {
        Member currentMember = currentMemberUtil.getCurrentMemberObject();

        houseWorkList.forEach(houseWork -> {
            houseWork.setAssign(currentMember, currentMember.getZipHapGroup());
        });
    }

}