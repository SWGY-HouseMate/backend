package com.swygbro.housemate.group.validator;

import com.swygbro.housemate.group.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class URIDuplicateValidatorTest {

    URIDuplicateValidator sut;

    @Mock
    GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        sut = new URIDuplicateValidator(groupRepository);
    }

    @Test
    void name() {
        sut.valid(any());

        verify(groupRepository).findByLinkId(any());
    }
}