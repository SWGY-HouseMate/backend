package com.swygbro.housemate.login.message;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUser { //구글(서드파티)로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
    public String id;
    public String email;
    public Boolean verified_email;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;

    @JsonCreator
    public GoogleUser(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email,
            @JsonProperty("verifiedEmail") Boolean verified_email,
            @JsonProperty("name") String name,
            @JsonProperty("givenName") String given_name,
            @JsonProperty("familyName") String family_name,
            @JsonProperty("picture") String picture,
            @JsonProperty("locale") String locale) {

        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.locale = locale;
    }
}