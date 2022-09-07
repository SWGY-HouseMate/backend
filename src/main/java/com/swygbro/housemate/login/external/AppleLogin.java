package com.swygbro.housemate.login.external;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.service.Login;

import static com.swygbro.housemate.login.domain.LoginType.애플;

public class AppleLogin implements Login {
    @Override
    public Object execute() {
        return null;
    }

    @Override
    public String getType() {
        return 애플.getValue();
    }
}
