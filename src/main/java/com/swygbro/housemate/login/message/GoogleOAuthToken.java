package com.swygbro.housemate.login.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleOAuthToken {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;


    @JsonCreator
    public GoogleOAuthToken(
            @JsonProperty("access_token") String access_token,
            @JsonProperty("expires_in") int expires_in,
            @JsonProperty("scope") String scope,
            @JsonProperty("token_type") String token_type,
            @JsonProperty("id_token") String id_token) {

        this.access_token = access_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.token_type = token_type;
        this.id_token = id_token;
    }
}