package com.swygbro.housemate.group.service;

import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.group.validator.URIDuplicateValidator;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupFactoryTest {

    GroupFactory sut;

    @Mock
    LinkCreator linkCreator;
    @Mock
    GroupRepository groupRepository;
    @Mock
    UUIDUtil uuidUtil;
    @Mock
    URIDuplicateValidator uriDuplicateValidator;
    @Mock
    CurrentMemberUtil currentMemberUtil;

    String uriId = "aaa";
    GroupResponse groupResponse = GroupResponse.of(uriId, "aaa", "aaa");

    @BeforeEach
    void setUp() {
        sut = new GroupFactory(linkCreator, groupRepository, uuidUtil, currentMemberUtil, uriDuplicateValidator);
    }

    @Test
    void createBy_group_link_uriDuplicate() {
//        given(linkCreator.executor(any(), any())).willThrow(IllegalStateException.class);
//
//        assertThatThrownBy(() -> sut.create(groupCreator)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void create_group_success() {
//        GroupResponse result = sut.create(groupCreator);
//
//        assertThat(result.getUriId().equals(groupResponse.getUriId()));
    }

    @Test
    void join_group_success() {

    }
}