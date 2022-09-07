package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.service.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.GOOGLE;

@Component
@RequiredArgsConstructor
public class GoogleLogin implements Login {

    @Value("${spring.OAuth2.google.url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.OAuth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.OAuth2.google.scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;
    private final HttpServletResponse response;

    @Override
    public Object execute(Map<String, String> additionInfo) {
        return null;
    }

    @Override
    public String getType() {
        return GOOGLE.getValue();
    }
}
