package com.swygbro.housemate.group.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InitValidatorTest {

    InitValidator sut;
    @Mock
    URIDuplicateValidator uriDuplicateValidator;

    @BeforeEach
    void setUp() {
        sut = new InitValidator(uriDuplicateValidator);
        sut.setValidator();
    }

    @Test
    void findValidatorByOne() {
        List<ValidatorURI> result = sut.get();

        assertThat(result.size()).isGreaterThanOrEqualTo(0);
    }
}