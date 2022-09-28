package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.GetSocialOAuthRes;
import com.swygbro.housemate.login.message.KakaoOAuthToken;
import com.swygbro.housemate.login.message.KakaoUser;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.login.service.Login;
import com.swygbro.housemate.security.jwt.JwtTokenProvider;
import com.swygbro.housemate.util.uuid.UUIDUtil;
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
    private final UUIDUtil uuidUtil;

    @Override
    public GetSocialOAuthRes execute(Map<String, String> additionInfo) throws JsonProcessingException {
        KakaoOAuthToken oAuthToken = kakaoService.getKakaoAccessToken(additionInfo.get("code"));

        KakaoUser kakaoUser = kakaoService.getKakaoUserInfo(oAuthToken.getAccess_token());

        Optional<Member> emailExists = memberRepository.findByMemberEmail(kakaoUser.getEmail());

        Member createMember = Member.builder()
                .memberId(uuidUtil.create())
                .memberEmail(kakaoUser.getEmail())
                .memberName(kakaoUser.getName())
                .memberProfilePicture(kakaoUser.getPicture())
                .memberAuthorityRoles(Collections.singletonList(DEFAULT))
                .memberLoginRole(KAKAO.getKey())
                .zipHapGroup(null)
                .build();

        if (emailExists.isEmpty()) {
            memberRepository.save(createMember);
        }

        String jwtToken = jwtTokenProvider.createToken(createMember);

        return GetSocialOAuthRes.of(jwtToken, "Bearer", oAuthToken.getAccess_token(), oAuthToken.getToken_type(), KAKAO.getKey(), createMember.getMemberId());
    }

    @Override
    public String getType() {
        return KAKAO.getValue();
    }
}
