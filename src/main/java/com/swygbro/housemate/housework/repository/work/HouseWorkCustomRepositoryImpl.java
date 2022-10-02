package com.swygbro.housemate.housework.repository.work;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.CycleInfo;
import com.swygbro.housemate.housework.message.HouseWorkByMember;
import com.swygbro.housemate.housework.message.HouseWorkInfo;
import com.swygbro.housemate.housework.message.HouseWorkMemberInfo;
import com.swygbro.housemate.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.swygbro.housemate.housework.domain.QHouseWork.houseWork;
import static com.swygbro.housemate.login.domain.QMember.member;

@RequiredArgsConstructor
public class HouseWorkCustomRepositoryImpl implements HouseWorkCustomRepository {

    private final JPQLQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    @Override
    public Optional<HouseWork> findByHouseWorkIdJoinManger(String houseWorkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(houseWork)
                        .join(houseWork.manager)
                        .fetchJoin()
                        .where(houseWork.houseWorkId.eq(houseWorkId))
                        .fetchOne());
    }

    @Override
    public HouseWorkByMember searchHouseWorkAtDateByGroupDSL(LocalDate startAt, LocalDate endAt, Group group) {
        GroupInfo groupInfo = modelMapper.map(group, GroupInfo.class);
        List<Member> memberList = queryFactory.selectFrom(member)
                .join(member.zipHapGroup).fetchJoin()
                .where(member.zipHapGroup.eq(group))
                .fetchAll()
                .fetch();

        List<HouseWorkMemberInfo> houseWorkMemberInfoList = new ArrayList<>();
        for (Member m : memberList) {
            List<HouseWork> houseWorkList = queryFactory.selectFrom(houseWork)
                    .join(houseWork.manager).fetchJoin()
                    .join(houseWork.cycle).fetchJoin()
                    .where(houseWork.today.between(startAt, endAt)
                            .and(houseWork.manager.eq(m)))
                    .fetchAll()
                    .fetch();

            List<HouseWorkInfo> houseWorkInfoList = new ArrayList<>();
            for (HouseWork work : houseWorkList) {
                houseWorkInfoList.add(HouseWorkInfo.builder()
                        .houseWorkId(work.getHouseWorkId())
                        .title(work.getTitle())
                        .difficulty(work.getDifficulty())
                        .today(work.getToday())
                        .isCompleted(work.getIsCompleted())
                        .isCycle(work.getIsCycle())
                        .cycleInfo(modelMapper.map(work.getCycle(), CycleInfo.class))
                        .build());
            }

            houseWorkMemberInfoList.add(HouseWorkMemberInfo.builder()
                    .memberId(m.getMemberId())
                    .memberEmail(m.getMemberEmail())
                    .memberLoginRole(m.getMemberLoginRole())
                    .memberAuthorityRoles(m.getMemberRole())
                    .houseWorkInfoList(houseWorkInfoList)
                    .build());
        }

        return HouseWorkByMember.builder()
                .houseWorkInfos(houseWorkMemberInfoList)
                .groupInfo(groupInfo)
                .build();
    }



}
