package com.swygbro.housemate.security.authectication;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
    }

}
