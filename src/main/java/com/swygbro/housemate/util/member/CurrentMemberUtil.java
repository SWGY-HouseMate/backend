package com.swygbro.housemate.util.member;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Component
@RequiredArgsConstructor
public class CurrentMemberUtil {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public Member getCurrentMemberObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return memberRepository.findByMemberEmail(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
    }

    public Member getCurrentMemberANDGroupObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else{
            username = principal.toString();
        }
        return memberRepository.findByEmailJoinFetchGroup(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
    }

    public MemberInfo getCurrentMemberInfoObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else{
            username = principal.toString();
        }
        Member member = memberRepository.findByMemberEmail(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        return modelMapper.map(member, MemberInfo.class);
    }
}
