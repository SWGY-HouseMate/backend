package com.swygbro.housemate.login.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetSocialOAuthRes { //클라이언트로 보낼 jwtToken, accessToken등이 담긴 객체

    private String jwtToken;
    private int user_num;
    private String accessToken;
    private String tokenType;
}