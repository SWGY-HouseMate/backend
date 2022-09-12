package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.service.LoginPage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.swygbro.housemate.login.domain.LoginType.KAKAO;

@Component
public class KakaoLoginPage implements LoginPage {

    @Override
    public void view(HttpServletResponse response) throws IOException {
        String RequestURL = getOauthRedirectURL();
        response.sendRedirect(RequestURL);
    }

    @Override
    public String viewGetType() {
        return KAKAO.getValue();
    }

    public String getOauthRedirectURL() {
        Map<String,Object> params = new HashMap<>();
        params.put("client_id", "44a3c35e7086a6cea10ebddd03df7aa7");
        params.put("redirect_uri", "http://localhost:8080/accounts/auth/kakao/callback");
        params.put("response_type", "code");

        String parameterString=params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));

        return "https://kauth.kakao.com/oauth/authorize"+"?"+ parameterString;
    }
}
