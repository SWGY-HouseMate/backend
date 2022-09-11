package com.swygbro.housemate.login.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoOAuthToken {

    private String token_type;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;


    @JsonCreator
    public KakaoOAuthToken(
            @JsonProperty("access_token") String access_token,
            @JsonProperty("expires_in") int expires_in,
            @JsonProperty("token_type") String token_type,
            @JsonProperty("refresh_token") String refresh_token,
            @JsonProperty("refresh_token_expires_in") int refresh_token_expires_in,
            @JsonProperty("scope") String scope) {

        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
        this.scope = scope;
    }
}
