package com.swygbro.housemate.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swygbro.housemate.login.external.GoogleLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;

import static com.swygbro.housemate.login.domain.LoginType.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OAutLoginFinderTest {

    OAutLoginFinder sut;

    @Mock
    GoogleLogin googleLogin;

    @BeforeEach
    void setUp() {
        sut = new OAutLoginFinder(googleLogin);
    }

    @Test
    void findBy_fined_googleLogin() {
        Login login = sut.findBy(GOOGLE);
        assertThat(login.getType()).isEqualTo("구글");
    }

    @Test
    void findBy_fined_KakaoLogin() {
        Login login = sut.findBy(KAKAO);
        assertThat(login.getType()).isEqualTo("카카오");
    }

    @Test
    void findBy_fined_AppleLogin() {
        Login login = sut.findBy(APPLE);
        assertThat(login.getType()).isEqualTo("애플");
    }
}