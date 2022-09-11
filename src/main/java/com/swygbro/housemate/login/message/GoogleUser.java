package com.swygbro.housemate.login.message;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class GoogleUser { //구글(서드파티)로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
    private String id;
    private String email;
    private Boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
    private String hd;

    @JsonCreator
    public GoogleUser(
            @JsonProperty("id") String id,
            @JsonProperty("email") String email,
            @JsonProperty("verifiedEmail") Boolean verified_email,
            @JsonProperty("name") String name,
            @JsonProperty("givenName") String given_name,
            @JsonProperty("familyName") String family_name,
            @JsonProperty("picture") String picture,
            @JsonProperty("locale") String locale,
            @JsonProperty("hd") String hd) {

        this.id = id;
        this.email = email;
        this.verified_email = verified_email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.locale = locale;
        this.hd = hd;
    }
}