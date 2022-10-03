package com.swygbro.housemate.login;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Component
@RequiredArgsConstructor
public class ManagedFactory {

    private final CurrentMemberUtil currentMemberUtil;

    private final MemberRepository memberRepository;

    public void assign(List<HouseWork> houseWorkList) {
        Member currentMember = currentMemberUtil.getCurrentMemberObject();
        Member byMemberEmailJPQL = memberRepository.findByEmailJoinFetchGroup(currentMember.getMemberEmail())
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));

        houseWorkList.forEach(houseWork -> {
            houseWork.setAssign(byMemberEmailJPQL, byMemberEmailJPQL.getZipHapGroup());
        });
    }

}
