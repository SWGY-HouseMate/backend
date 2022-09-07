package com.swygbro.housemate.login.service;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.external.AppleLogin;
import com.swygbro.housemate.login.external.GoogleLogin;
import com.swygbro.housemate.login.external.KakaoLogin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.*;

@Component
public class LoginFinder {

    Map<LoginType, Login> loginMap = new HashMap<>();

    public LoginFinder() {
        loginMap.put(구글, new GoogleLogin());
        loginMap.put(카카오, new KakaoLogin());
        loginMap.put(애플, new AppleLogin());
    }

    public Login findBy(LoginType loginType) {
        return loginMap.get(loginType);
    }
}
