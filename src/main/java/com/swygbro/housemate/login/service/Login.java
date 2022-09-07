package com.swygbro.housemate.login.service;

import java.io.IOException;
import java.util.Map;

public interface Login {
    String getType();
    Object execute(Map<String, String> additionInfo) throws IOException;
}
