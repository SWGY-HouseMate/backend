package com.swygbro.housemate.login.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginFinderTest {

    LoginFinder sut;

    @BeforeEach
    void setUp() {
        sut = new LoginFinder();
    }

    // MethodName_StateUnderTest_ExpectedBehavior
    // isAdult_AgeLessThan18_False
    // withdrawMoney_InvalidAccount_ExceptionThrown
    // admitStudent_MissingMandatoryFields_FailToAdmit

    @Test
    void appleLogin_success() {

    }
}