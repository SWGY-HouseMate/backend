package com.swygbro.housemate.login.message;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class GetSocialOAuthRes { //클라이언트로 보낼 jwtToken, accessToken등이 담긴 객체

    String zipHapJwtToken;
    String zipHapTokenType;
    String socialLoginAccessToken;
    String socialLoginTokenType;
    String socialLoginLoginType;

}