package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.service.Login;

import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.KAKAO;

public class KakaoLogin implements Login {
    @Override
    public Object execute(Map<String, String> additionInfo) {
        return null;
    }

    @Override
    public String getType() {
        return KAKAO.getValue();
    }
}
