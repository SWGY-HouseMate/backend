package com.swygbro.housemate.controller;

import com.swygbro.housemate.login.domain.LoginType;
import com.swygbro.housemate.login.external.GoogleOauthService;
import com.swygbro.housemate.login.external.KakaoService;
import com.swygbro.housemate.login.message.GetSocialOAuthRes;
import com.swygbro.housemate.login.service.Login;
import com.swygbro.housemate.login.service.LoginPage;
import com.swygbro.housemate.login.service.LoginPageFinder;
import com.swygbro.housemate.login.service.OAutLoginFinder;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
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

    private final ResponseService responseService;
    private final OAutLoginFinder oAutLoginFinder;
    private final LoginPageFinder loginPageFinder;
    private final KakaoService kakaoService;
    private final GoogleOauthService googleOauthService;

    /**
     * 로그인 페이지
     * [GET] /accounts/auth/{loginType}
     */
    @GetMapping("/auth/{loginType}")
    public void loginPage(@PathVariable String loginType, HttpServletResponse response) throws IOException {
        LoginPage findLoginPage = loginPageFinder.findBy(LoginType.valueOf(loginType.toUpperCase()));
        findLoginPage.view(response);
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public SingleResult<GetSocialOAuthRes> callbackLogin (@PathVariable String socialLoginType, @RequestParam String code) throws IOException {
        Login by = oAutLoginFinder.findBy(LoginType.valueOf(socialLoginType.toUpperCase()));
        Map<String, String> info = new HashMap<>();
        info.put("code", code);

        return responseService.getSingleResult(by.execute(info));
    }

    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/logout")
    public SingleResult<String> logout (@PathVariable String socialLoginType, @RequestParam String access_token) throws IOException {
        int isLogout = 0;

        if (socialLoginType.equalsIgnoreCase("kakao")) {
            isLogout = kakaoService.logout(access_token);
        } else if (socialLoginType.equalsIgnoreCase("google")) {
            isLogout = googleOauthService.logout(access_token);
        }

        if (isLogout == 1) {
            return responseService.getSingleResult("성공하였습니다.");
        }

        return responseService.getSingleResult("실패하였습니다.");
    }
}
