package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.GetSocialOAuthRes;
import com.swygbro.housemate.login.message.GoogleOAuthToken;
import com.swygbro.housemate.login.message.GoogleUser;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.login.service.Login;
import com.swygbro.housemate.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.swygbro.housemate.login.domain.LoginType.GOOGLE;
import static com.swygbro.housemate.login.domain.MemberType.DEFAULT;

@Component
@RequiredArgsConstructor
public class GoogleLogin implements Login {

    private final GoogleOauthService googleOauthService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Object execute(Map<String, String> additionInfo) throws JsonProcessingException {
        GoogleOAuthToken oAuthToken = googleOauthService.getGoogleAccessToken(additionInfo.get("code"));

        GoogleUser googleUser = googleOauthService.getUserInfo(oAuthToken);

        Optional<Member> emailExists = memberRepository.findByEmail(googleUser.getEmail());

        Member createMember = Member.builder()
                .email(googleUser.getEmail())
                .memberRoles(Collections.singletonList(DEFAULT))
                .loginRole(GOOGLE.getKey())
                .build();

        if (emailExists.isEmpty()) {
            memberRepository.save(createMember);
        }

        String jwtToken = jwtTokenProvider.createToken(createMember);

        return GetSocialOAuthRes.of(jwtToken, "Bearer", oAuthToken.getAccess_token(), oAuthToken.getToken_type(), GOOGLE.getKey());
    }

    @Override
    public String getType() {
        return GOOGLE.getValue();
    }
}
