package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.service.Login;

import static com.swygbro.housemate.login.domain.LoginType.카카오;

public class KakaoLogin implements Login {
    @Override
    public Object execute() {
        return null;
    }

    @Override
    public String getType() {
        return 카카오.getValue();
    }
}
