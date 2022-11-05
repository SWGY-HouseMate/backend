package com.swygbro.housemate.login.service;

import com.swygbro.housemate.login.external.GoogleLogin;
import com.swygbro.housemate.login.external.KakaoLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static com.swygbro.housemate.login.domain.LoginType.GOOGLE;
import static com.swygbro.housemate.login.domain.LoginType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OAutLoginFinderTest {
    OAutLoginFinder sut;

    @Mock
    GoogleLogin googleLogin;
    @Mock
    KakaoLogin kakaoLogin;

    @BeforeEach
    void setUp() {
        sut = new OAutLoginFinder(googleLogin, kakaoLogin);
    }

    @Test
    void findBy_fined_googleLogin() throws IOException {
        sut.findBy(GOOGLE).execute(Map.of());

        verify(googleLogin).execute(Map.of());
        verifyNoInteractions(kakaoLogin);
    }

    @Test
    void findBy_fined_googleLogin_getType() {
        given(sut.findBy(GOOGLE).getType()).willReturn(GOOGLE.getValue());

        Login login = sut.findBy(GOOGLE);
        
        assertThat(login.getType()).isEqualTo(GOOGLE.getValue());
    }

    @Test
    void findBy_fined_kakaoLogin() throws IOException {
        sut.findBy(KAKAO).execute(Map.of());

        verify(kakaoLogin).execute(Map.of());
        verifyNoInteractions(googleLogin);
    }

    @Test
    void findBy_fined_kakaoLogin_getType() {
        given(sut.findBy(KAKAO).getType()).willReturn(KAKAO.getValue());

        Login login = sut.findBy(KAKAO);

        assertThat(login.getType()).isEqualTo(KAKAO.getValue());
    }
}