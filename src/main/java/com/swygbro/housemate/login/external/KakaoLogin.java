package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.GetSocialOAuthRes;
import com.swygbro.housemate.login.message.KakaoOAuthToken;
import com.swygbro.housemate.login.message.KakaoUser;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.login.service.Login;
import com.swygbro.housemate.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.swygbro.housemate.login.domain.LoginType.KAKAO;
import static com.swygbro.housemate.login.domain.MemberType.DEFAULT;

@Component
@RequiredArgsConstructor
public class KakaoLogin implements Login {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;

    @Override
    public Object execute(Map<String, String> additionInfo) throws JsonProcessingException {
        KakaoOAuthToken oAuthToken = kakaoService.getKakaoAccessToken(additionInfo.get("code"));

        KakaoUser kakaoUser = kakaoService.getKakaoUserInfo(oAuthToken.getAccess_token());

        Optional<Member> emailExists = memberRepository.findByEmail(kakaoUser.getEmail());

        Member createMember = Member.builder()
                .email(kakaoUser.getEmail())
                .memberRoles(Collections.singletonList(DEFAULT))
                .loginRole(KAKAO.getKey())
                .build();

        if (emailExists.isEmpty()) {
            memberRepository.save(createMember);
        }

        String jwtToken = jwtTokenProvider.createToken(createMember);

        return GetSocialOAuthRes.of(jwtToken, "Bearer", oAuthToken.getAccess_token(), oAuthToken.getToken_type(), KAKAO.getKey());
    }

    @Override
    public String getType() {
        return KAKAO.getValue();
    }
}
