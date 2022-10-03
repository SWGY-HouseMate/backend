package com.swygbro.housemate.group.service;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupCreator;
import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.group.validator.URIDuplicateValidator;
import com.swygbro.housemate.group.validator.ValidatorURI;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class GroupFactoryTest {

    GroupFactory sut;

    @Mock
    LinkCreator linkCreator;
    @Mock
    GroupRepository groupRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    UUIDUtil uuidUtil;
    @Mock
    URIDuplicateValidator uriDuplicateValidator;
    @Mock
    GroupCreator groupCreator;
    @Mock
    ValidatorURI validatorURI;
    @Mock
    Member member;
    @Mock
    Group group;
    @Mock
    CurrentMemberUtil currentMemberUtil;
    @Mock
    ModelMapper modelMapper;

    String uriId = "aaa";
    GroupResponse groupResponse = GroupResponse.of(uriId, "aaa", "aaa");

    @BeforeEach
    void setUp() {
        sut = new GroupFactory(linkCreator, groupRepository, memberRepository, uuidUtil, modelMapper, uriDuplicateValidator);
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