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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static com.swygbro.housemate.login.domain.LoginType.GOOGLE;

@Component
@RequiredArgsConstructor
public class GoogleLogin implements Login {

    private final GoogleOauthService googleOauthService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Object execute(Map<String, String> additionInfo) throws JsonProcessingException {
        // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴 (deserialization하여 자바 객체로 변경)
        GoogleOAuthToken oAuthToken = googleOauthService.getGoogleAccessToken(additionInfo.get("code"));

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다. (deserialization하여 자바 객체로 변경)
        GoogleUser googleUser = googleOauthService.getUserInfo(oAuthToken);

        //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
        Optional<Member> emailExists = memberRepository.findByEmail(googleUser.getEmail());

        if (emailExists.isEmpty()) {
            System.out.println("회원 정보 저장");
            // 회원 정보 저장 하기
        }

        //서버에 user가 존재하면 앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
        String jwtToken = jwtTokenProvider.createToken(null);

        //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
        GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, 1, oAuthToken.getAccess_token(), oAuthToken.getToken_type());
        return getSocialOAuthRes;
    }

    @Override
    public String getType() {
        return GOOGLE.getValue();
    }
}
