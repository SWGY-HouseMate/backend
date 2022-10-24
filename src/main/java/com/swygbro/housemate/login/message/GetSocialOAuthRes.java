package com.swygbro.housemate.login.message;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class GetSocialOAuthRes {

    String zipHapJwtToken;
    String zipHapTokenType;
    String socialLoginAccessToken;
    String socialLoginTokenType;
    String socialLoginLoginType;
    String memberId;

}