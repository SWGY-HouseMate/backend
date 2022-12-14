package com.swygbro.housemate.util.member;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.repository.GroupRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.그룹을_찾을_수_없습니다;
import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.멤버를_찾을_수_없습니다;

@Component
@RequiredArgsConstructor
public class CurrentMemberUtil {
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    public MemberDto getCurrentMemberDtoObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Member member = memberRepository.findByMemberEmail(username)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        return MemberDto.of(member.getMemberId(), member.getMemberEmail(), member.getMemberName(), member.getMemberProfilePicture(), member.getMemberLoginRole(), member.getMemberRole());
    }

    public Member getCurrentMemberObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return memberRepository.findByMemberEmail(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
    }

    public Member getCurrentMemberANDGroupObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return memberRepository.findByEmailJoinFetchGroup(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
    }

    public CurrentMemberInfo getCurrentMemberInfoAndGroupInfoObject() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Member member = memberRepository.findByEmailJoinFetchGroup(username).orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        Group group = groupRepository.findByLinkIdJoinFetchOwner(member.getZipHapGroup().getLinkId())
                .orElseThrow(() -> new DataNotFoundException(그룹을_찾을_수_없습니다));

        GroupInfo groupInfo = modelMapper.map(group, GroupInfo.class);
        MemberInfo memberInfo = modelMapper.map(member, MemberInfo.class);

        return CurrentMemberInfo.of(groupInfo, memberInfo);
    }

    public GroupPersonInfo getMembersOfTheGroup(Group group) {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Member member_me = memberRepository.findByMemberEmail(username)
                .orElseThrow(() -> new DataNotFoundException(멤버를_찾을_수_없습니다));
        MemberInfo memberInfo_me = modelMapper.map(member_me, MemberInfo.class);

        List<Member> member_authorList = memberRepository.findByZipHapGroup(group)
                .stream()
                .filter(m -> !m.getMemberEmail().equals(member_me.getMemberEmail()))
                .collect(Collectors.toList());
        if (member_authorList.size() == 0) {
            return GroupPersonInfo.of(memberInfo_me, null);
        }

        MemberInfo memberInfo_author = modelMapper.map(member_authorList.get(0), MemberInfo.class);
        return GroupPersonInfo.of(memberInfo_me, memberInfo_author);
    }
}
