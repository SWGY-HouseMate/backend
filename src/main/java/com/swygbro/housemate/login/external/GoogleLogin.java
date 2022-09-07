package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.service.Login;

import static com.swygbro.housemate.login.domain.LoginType.구글;

public class GoogleLogin implements Login {

    @Override
    public Object execute() {
        return null;
    }

    @Override
    public String getType() {
        return 구글.getValue();
    }
}
