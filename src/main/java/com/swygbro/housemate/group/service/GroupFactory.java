package com.swygbro.housemate.group.service;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupCreator;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.group.validator.URIDuplicateValidator;
import com.swygbro.housemate.group.validator.ValidatorURI;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.repository.MemberRepository;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.그룹을_찾을_수_없습니다;
import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;
import static com.swygbro.housemate.login.domain.MemberType.OWNER;

@Service
public class GroupFactory {
    private final String BASE_URL = "http://housework-backend.ap-northeast-2.elasticbeanstalk.com";
    private final List<ValidatorURI> validators = new ArrayList<>();
    private final LinkCreator linkCreator;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final UUIDUtil uuidUtil;
    private final ModelMapper modelMapper;
    private final CurrentMemberUtil currentMemberUtil;

    public GroupFactory(LinkCreator linkCreator,
                        GroupRepository groupRepository,
                        MemberRepository memberRepository,
                        UUIDUtil uuidUtil,
                        ModelMapper modelMapper,
                        CurrentMemberUtil currentMemberUtil,
                        URIDuplicateValidator uriDuplicateValidator) {
        this.linkCreator = linkCreator;
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
        this.uuidUtil = uuidUtil;
        this.modelMapper = modelMapper;
        this.currentMemberUtil = currentMemberUtil;
        validators.add(uriDuplicateValidator);
    }

    @Transactional
    public GroupResponse create(GroupCreator groupCreator) {
        Member currentMemberObject = currentMemberUtil.getCurrentMemberObject();
        String linkId = linkCreator.executor(currentMemberObject.getMemberId(), validators);
        Group group = groupRepository.save(groupCreator.create(uuidUtil.create(), linkId, currentMemberObject));

        currentMemberObject.updateRole(OWNER);
        group.applyMember(currentMemberObject);

        currentMemberObject.updateName(group.getGroupName());
        return GroupResponse.of(linkId, group.createAt(), BASE_URL +"/group/join/" + linkId);
    }

    @Transactional
    public GroupResponse join(String likeId, String memberName, String memberEmail) {
        Member addMember = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));

        Group group = groupRepository.findByLinkId(likeId)
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

        group.applyMember(addMember);
        addMember.updateName(memberName);

        return GroupResponse.of(group.getLinkId(), group.createAt(), BASE_URL + "/group/join/" + group.getLinkId());
    }

    public GroupInfo info(String likeId) {
        return groupRepository.findByLinkIdJoinFetchOwner(likeId)
                .map(m -> modelMapper.map(m, GroupInfo.class))
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));
    }
}
