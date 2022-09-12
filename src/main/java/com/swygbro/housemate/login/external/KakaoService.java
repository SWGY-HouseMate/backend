package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.message.KakaoOAuthToken;
import com.swygbro.housemate.login.message.KakaoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoService {

    @Value("${spring.OAuth2.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.OAuth2.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.OAuth2.kakao.token}")
    private String KAKAO_TOKEN_REQUEST_URL;

    @Value("${spring.OAuth2.kakao.user-info}")
    private String KAKAO_USER_INFO_REQUEST_URL;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOAuthToken getKakaoAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_REQUEST_URL, request, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return objectMapper.readValue(responseEntity.getBody(), KakaoOAuthToken.class);
        }

        return null;
    }

    public KakaoUser getKakaoUserInfo(String access_token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_USER_INFO_REQUEST_URL, request, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return objectMapper.readValue(responseEntity.getBody(), KakaoUser.class);
        }

        return null;
    }
}
