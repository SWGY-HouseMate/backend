package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.service.LoginPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoLoginPage implements LoginPage {

    @Value("${spring.OAuth2.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.OAuth2.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.OAuth2.kakao.page-uri}")
    private String PAGE_URI;

    @Override
    public void view(HttpServletResponse response) throws IOException {
        String RequestURL = getOauthRedirectURL();
        response.sendRedirect(RequestURL);
    }

    public String getOauthRedirectURL() {
        Map<String,Object> params = new HashMap<>();
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        params.put("response_type", "code");

        String parameterString=params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));

        return PAGE_URI + "?" + parameterString;
    }
}
