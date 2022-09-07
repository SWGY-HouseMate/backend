package com.swygbro.housemate.controller;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.external.GoogleLoginPage;
import com.swygbro.housemate.login.service.Login;
import com.swygbro.housemate.login.service.OAutLoginFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class LoginController {

    private final GoogleLoginPage googleLoginPage;
    private final OAutLoginFinder oAutLoginFinder;

    /**
     * 구글 로그인 페이지 (테스트 용도)
     * [POST] /accounts/auth
     * @return void
     */
    @GetMapping("/auth/{loginType}")
    public void login(@PathVariable String loginType, HttpServletResponse response) throws IOException {
        System.out.println("loginType = " + loginType);
        googleLoginPage.execute(response);
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */

    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public Object callback (@PathVariable String socialLoginType, @RequestParam String code) throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        Login by = oAutLoginFinder.findBy(LoginType.valueOf(socialLoginType.toUpperCase()));
        Map<String, String> info = new HashMap<>();
        info.put("code", code);

        return by.execute(info);
    }
}
