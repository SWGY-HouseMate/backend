package com.swygbro.housemate.util.member;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentMemberUtil {

    private final MemberRepository memberRepository;

    public String getCurrentMemberString() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else{
            username = principal.toString();
        }
        System.out.println("====================================================");
        System.out.println("username = " + username);
        return username;
    }

    public Member getCurrentMemberObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else{
            username = principal.toString();
        }
        return memberRepository.findByMemberEmail(username).orElseThrow(null);
    }
}
