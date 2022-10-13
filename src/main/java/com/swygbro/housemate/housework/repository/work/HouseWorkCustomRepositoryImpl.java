package com.swygbro.housemate.housework.repository.work;

import com.querydsl.jpa.JPQLQueryFactory;
import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.message.*;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.COMPLETED;
import static com.swygbro.housemate.housework.domain.QHouseWork.houseWork;
import static com.swygbro.housemate.login.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class HouseWorkCustomRepositoryImpl implements HouseWorkCustomRepository {

    private final JPQLQueryFactory queryFactory;
    private final ModelMapper modelMapper;

    @Override
    public Optional<HouseWork> searchHouseWorkIdJoinManger(String houseWorkId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(houseWork)
                        .join(houseWork.manager)
                        .fetchJoin()
                        .where(houseWork.houseWorkId.eq(houseWorkId))
                        .fetchOne());
    }

    @Override
    public HouseWorkByMember searchHouseWorkAtDateByGroup(LocalDate startAt, LocalDate endAt, Group group) {
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
                CycleInfo cycleInfo = CycleInfo.builder()
                        .cycleId(work.getCycle().getCycleId())
                        .cycleType(work.getCycle().getCycleType())
                        .props(work.getCycle().getProps())
                        .startAt(work.getCycle().getStartAt())
                        .endAt(work.getCycle().getEndAt())
                        .build();

                houseWorkInfoList.add(HouseWorkInfo.builder()
                        .houseWorkId(work.getHouseWorkId())
                        .title(work.getTitle())
                        .difficulty(work.getDifficulty())
                        .today(work.getToday())
                        .houseWorkStatusType(work.getHouseWorkStatusType())
                        .isCycle(work.getIsCycle())
                        .cycleInfo(cycleInfo)
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
                .build();
    }

    @Override
    public HouseWorkCountForGroup searchHouseWorkCountByMember(LocalDate startAt, LocalDate endAt, Group group) {
        List<Member> memberList = queryFactory.selectFrom(member)
                .join(member.zipHapGroup).fetchJoin()
                .where(member.zipHapGroup.eq(group))
                .fetchAll()
                .fetch();

        List<HouseWorkCountInfo> houseWorkCountInfoList = new ArrayList<>();
        for (Member m : memberList) {
            MemberInfo memberInfo = modelMapper.map(m, MemberInfo.class);
            Long completedCount = queryFactory.select(houseWork.count())
                    .from(houseWork)
                    .where(houseWork.today.between(startAt, endAt)
                            .and(houseWork.manager.eq(m))
                            .and(houseWork.houseWorkStatusType.eq(COMPLETED)))
                    .fetchOne();

            Long allCount = queryFactory.select(houseWork.count())
                    .from(houseWork)
                    .where(houseWork.today.between(startAt, endAt)
                            .and(houseWork.manager.eq(m)))
                    .fetchOne();

            houseWorkCountInfoList.add(HouseWorkCountInfo.builder()
                    .memberInfo(memberInfo)
                    .completedCount(completedCount)
                    .allCount(allCount)
                    .build());
        }

        return HouseWorkCountForGroup.of(houseWorkCountInfoList);
    }

    @Override
    public List<HouseWork> searchHouseWorkByToday(LocalDate now) {
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        return queryFactory.selectFrom(houseWork)
                .join(houseWork.manager).fetchJoin()
                .join(houseWork.group).fetchJoin()
                .join(houseWork.cycle).fetchJoin()
                .where(houseWork.today.between(start, end))
                .fetchAll()
                .fetch();
    }

    @Override
    public Long countByMember(LocalDate now, Member member) {
        LocalDate start = now.withDayOfMonth(1);

        return queryFactory.selectFrom(houseWork)
                .where(
                        houseWork.today.between(start, now),
                        houseWork.manager.memberId.eq(member.getMemberId())
                ).fetchCount();
    }

    @Override
    public List<HouseWork> searchCalculateMostHouseWork(LocalDate now) {
        LocalDate start = now.withDayOfMonth(1);

        return queryFactory.selectFrom(houseWork)
                .join(houseWork.manager).fetchJoin()
                .join(houseWork.cycle).fetchJoin()
                .join(houseWork.group).fetchJoin()
                .where(
                        houseWork.today.between(start, now)
                ).fetchAll()
                .fetch();
    }


}
