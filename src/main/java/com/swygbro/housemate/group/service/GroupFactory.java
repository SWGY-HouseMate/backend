package com.swygbro.housemate.group.service;

import com.swygbro.housemate.exception.badrequest.BadRequestException;
import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupCreator;
import com.swygbro.housemate.group.message.GroupInfoByAll;
import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.member.GroupPersonInfo;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.swygbro.housemate.exception.badrequest.BadRequestType.그룹에_이미_참여하였습니다;
import static com.swygbro.housemate.exception.badrequest.BadRequestType.그룹이_1개_이상_생성_되었습니다;
import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.그룹을_찾을_수_없습니다;

@Service
@RequiredArgsConstructor
public class GroupFactory {
    @Value("${spring.backend.base-url}")
    private String BASE_URL;
    private final GroupRepository groupRepository;
    private final LinkCreator linkCreator;
    private final UUIDUtil uuidUtil;
    private final CurrentMemberUtil currentMemberUtil;

    @Transactional
    public GroupResponse create(GroupCreator groupCreator) {
        Member currentMemberObject = currentMemberUtil.getCurrentMemberObject();
        if (currentMemberObject.getZipHapGroup() != null) {
            throw new BadRequestException(그룹이_1개_이상_생성_되었습니다);
        }

        String linkId = linkCreator.executor(currentMemberObject.getMemberId());
        Group group = groupRepository.save(
                groupCreator.create(uuidUtil.create(), linkId, currentMemberObject)
        );

        group.ownerGroupCreateFlow(currentMemberObject);
        currentMemberObject.ownerGroupCreateFlow(groupCreator.getMemberName());

        return GroupResponse.of(linkId, group.createAt(), BASE_URL +"/group/join/" + linkId);
    }

    @Transactional
    public GroupResponse join(String likeId, String memberName) {
        Member addMember = currentMemberUtil.getCurrentMemberObject();

        if (addMember.getZipHapGroup() != null) {
            throw new BadRequestException(그룹에_이미_참여하였습니다);
        }

        Group group = groupRepository.findByLinkId(likeId)
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

        group.memberGroupJoinFlow(addMember);
        addMember.memberGroupJoinFlow(memberName);

        return GroupResponse.of(group.getLinkId(), group.createAt(), BASE_URL + "/group/join/" + group.getLinkId());
    }

    public GroupInfoByAll info(String likeId) {
        Group group = groupRepository.findByLinkIdJoinFetchOwner(likeId)
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

        GroupPersonInfo membersOfTheGroup = currentMemberUtil.getMembersOfTheGroup(group);

        return GroupInfoByAll.of(group.getZipHapGroupId(), group.getGroupName(), group.getLinkId(), membersOfTheGroup);
    }
}
