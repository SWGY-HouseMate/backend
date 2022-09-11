package com.swygbro.housemate.login.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.message.GoogleOAuthToken;
import com.swygbro.housemate.login.message.KakaoOAuthToken;
import com.swygbro.housemate.login.message.KakaoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoOAuthToken getKakaoAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "44a3c35e7086a6cea10ebddd03df7aa7");
        params.add("redirect_uri", "http://localhost:8080/accounts/auth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return objectMapper.readValue(responseEntity.getBody(), KakaoOAuthToken.class);
        }

        return null;
    }

    public KakaoUser getKakaoUserInfo(String access_token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", request, String.class);

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            return objectMapper.readValue(responseEntity.getBody(), KakaoUser.class);
        }

        return null;
    }
}
