package com.swygbro.housemate.login.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class KakaoUser {
    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    public String getEmail() {
        return this.kakao_account.getEmail();
    }

    @JsonCreator
    public KakaoUser(
            @JsonProperty("id") Long id,
            @JsonProperty("connected_at") String connected_at,
            @JsonProperty("kakao_account") KakaoAccount kakao_account) {

        this.id = id;
        this.connected_at = connected_at;
        this.kakao_account = kakao_account;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown=true)
    static class KakaoAccount {
        private String name;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;

        @JsonCreator
        public KakaoAccount(
                @JsonProperty("name") String name,
                @JsonProperty("is_email_valid") Boolean is_email_valid,
                @JsonProperty("is_email_verified") Boolean is_email_verified,
                @JsonProperty("email") String email) {

            this.name = name;
            this.is_email_valid = is_email_valid;
            this.is_email_verified = is_email_verified;
            this.email = email;
        }
    }
}
