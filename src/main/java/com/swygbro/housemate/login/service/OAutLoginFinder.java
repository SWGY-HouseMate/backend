package com.swygbro.housemate.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.external.AppleLogin;
import com.swygbro.housemate.login.external.GoogleLogin;
import com.swygbro.housemate.login.external.KakaoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.*;

@Component
public class OAutLoginFinder {

    Map<LoginType, Login> loginMap = new HashMap<>();

    public OAutLoginFinder(GoogleLogin googleLogin) {
        loginMap.put(GOOGLE, googleLogin);
        loginMap.put(KAKAO, new KakaoLogin());
        loginMap.put(APPLE, new AppleLogin());
    }

    public Login findBy(LoginType loginType) {
        return loginMap.get(loginType);
    }
}
