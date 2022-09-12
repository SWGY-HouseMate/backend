package com.swygbro.housemate.login.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface LoginPage {

    void view(HttpServletResponse response) throws IOException;

}
