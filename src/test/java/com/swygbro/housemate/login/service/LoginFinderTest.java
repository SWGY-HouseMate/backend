package com.swygbro.housemate.login.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.swygbro.housemate.login.domain.LoginType.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LoginFinderTest {

    LoginFinder sut;

    @BeforeEach
    void setUp() {
        sut = new LoginFinder();
    }

    @Test
    void findBy_fined_googleLogin() {
        Login login = sut.findBy(구글);
        assertThat(login.getType()).isEqualTo("GOOGLE");
    }

    @Test
    void findBy_fined_KakaoLogin() {
        Login login = sut.findBy(카카오);
        assertThat(login.getType()).isEqualTo("KAKAO");
    }

    @Test
    void findBy_fined_AppleLogin() {
        Login login = sut.findBy(애플);
        assertThat(login.getType()).isEqualTo("APPLE");
    }
}