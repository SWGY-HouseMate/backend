package com.swygbro.housemate.login.service;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.external.GoogleLoginPage;
import com.swygbro.housemate.login.external.KakaoLoginPage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.GOOGLE;
import static com.swygbro.housemate.login.domain.LoginType.KAKAO;

@Component
public class LoginPageFinder {
    Map<LoginType, LoginPage> pages = new HashMap<>();

    public LoginPageFinder(GoogleLoginPage googleLoginPage, KakaoLoginPage kakaoLoginPage) {
        pages.put(GOOGLE, googleLoginPage);
        pages.put(KAKAO, kakaoLoginPage);
    }

    public LoginPage findBy(LoginType loginType) {
        return pages.get(loginType);
    }
}
