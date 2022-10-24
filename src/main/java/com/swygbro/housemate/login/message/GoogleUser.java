package com.swygbro.housemate.login.message;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
public class GoogleUser {
    private String id;
    private String email;
    private Boolean verified_email;
    private String name; // 전체 이름
    private String given_name; // 두번째 이름
    private String family_name; // 첫번째 이름
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