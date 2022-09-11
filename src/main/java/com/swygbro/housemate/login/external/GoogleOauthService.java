package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.message.GoogleOAuthToken;
import com.swygbro.housemate.login.message.GoogleUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOauthService {

    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.OAuth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthToken getGoogleAccessToken(String code) throws JsonProcessingException {
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params,String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            System.out.println("response.getBody() = " + responseEntity.getBody());
            GoogleOAuthToken googleOAuthToken = objectMapper.readValue(responseEntity.getBody(), GoogleOAuthToken.class);
            return googleOAuthToken;
        }

        return null;
    }

    public GoogleUser getUserInfo(GoogleOAuthToken oAuthToken) throws JsonProcessingException {
        String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return objectMapper.readValue(response.getBody(), GoogleUser.class);
    }

}