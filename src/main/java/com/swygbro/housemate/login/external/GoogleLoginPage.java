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
public class GoogleLoginPage implements LoginPage {

    @Value("${spring.OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.OAuth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    @Override
    public void view(HttpServletResponse response) throws IOException {
        String RequestURL = getOauthRedirectURL();
        response.sendRedirect(RequestURL);
    }

    /*
     * parameter를 형식에 맞춰 구성해주는 함수
     */
    private String getOauthRedirectURL(){
        Map<String,Object> params=new HashMap<>();
        params.put("scope",GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type","code");
        params.put("client_id",GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri",GOOGLE_SNS_CALLBACK_URL);

        String parameterString=params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_LOGIN_URL + "?" + parameterString;
    }
}
