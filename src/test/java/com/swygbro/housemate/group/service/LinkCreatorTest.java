package com.swygbro.housemate.group.service;

import com.swygbro.housemate.group.validator.InitValidator;
import com.swygbro.housemate.group.validator.URIDuplicateValidator;
import com.swygbro.housemate.group.validator.ValidatorURI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LinkCreatorTest {
    final String URL = "sl39dk-eddld39383";
    final String USER_ID = "aldw3038-aldi3l";

    LinkCreator sut;

    @Mock
    InitValidator initValidator;
    @Mock
    URIDuplicateValidator uriDuplicateValidator;
    List<ValidatorURI> validatorURIList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        sut = new LinkCreator(initValidator);
        initValidator.setValidator();
        validatorURIList.add(uriDuplicateValidator);
    }

    @Test
    void name() {
        given(initValidator.get()).willReturn(validatorURIList);
        given(uriDuplicateValidator.valid(URL)).willReturn(true);

        sut.executor(USER_ID);

        verify(initValidator).get();
        verify(uriDuplicateValidator).valid(URL);
    }

    @Test
    void name2() {
        given(initValidator.get()).willReturn(List.of(uriDuplicateValidator));
        given(sut.executor(USER_ID)).willReturn(URL);

        String executor = sut.executor(USER_ID);

        assertThat(executor).isEqualTo(URL);
    }
}