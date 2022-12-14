package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.exception.badrequest.BadRequestException;
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

import static com.swygbro.housemate.exception.badrequest.BadRequestType.GOOGLE_LOGIN_COMMUNICATION_FAIL;

@Component
@RequiredArgsConstructor
public class GoogleOauthService {

    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.OAuth2.google.callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.OAuth2.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${spring.OAuth2.google.token}")
    private String GOOGLE_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.google.user-info}")
    private String GOOGLE_USERINFO_REQUEST_URL;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthToken getGoogleAccessToken(String code) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL, params,String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return objectMapper.readValue(responseEntity.getBody(), GoogleOAuthToken.class);
        }

        throw new BadRequestException(GOOGLE_LOGIN_COMMUNICATION_FAIL);
    }

    public GoogleUser getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);

        return objectMapper.readValue(response.getBody(), GoogleUser.class);
    }

}
