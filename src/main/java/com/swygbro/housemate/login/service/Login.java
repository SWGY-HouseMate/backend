package com.swygbro.housemate.login.service;

import com.swygbro.housemate.login.message.GetSocialOAuthRes;

import java.io.IOException;
import java.util.Map;

public interface Login {

    String getType();
    GetSocialOAuthRes execute(Map<String, String> additionInfo) throws IOException;

}
