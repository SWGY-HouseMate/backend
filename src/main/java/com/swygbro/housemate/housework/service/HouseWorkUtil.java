package com.swygbro.housemate.housework.service;

import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.housework.domain.HouseWork;
import com.swygbro.housemate.housework.domain.HouseWorkStatusType;
import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.repository.work.HouseWorkRepository;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.condition.CycleCondition;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.집안일을_찾을_수_없습니다;
import static com.swygbro.housemate.housework.domain.HouseWorkStatusType.DEFAULT;

@Component
@RequiredArgsConstructor
public class HouseWorkUtil {
    private final CycleCondition cycleCondition;
    private final UUIDUtil uuidUtil;
    private final HouseWorkRepository houseWorkRepository;
    private final CurrentMemberUtil currentMemberUtil;

    public Boolean getCondition(CreateHouseWork createHouseWork) {
        return cycleCondition.isSatisfiedBy(createHouseWork);
    }

    public long startAtAndEndAtDiff(CreateHouseWork createHouseWork) {
        LocalDate startAt = createHouseWork.getStartAt();
        LocalDate endAt = createHouseWork.getEndAt();

        Date startDate = dateConverter(startAt);
        Date endDate = dateConverter(endAt);

        long differenceInMillis = endDate.getTime() - startDate.getTime();
        return  (differenceInMillis / (24 * 60 * 60 * 1000));
    }

    public Date dateConverter(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        return date;
    }

    public HouseWork createHouseWork(CreateHouseWork createHouseWork) {
        return HouseWork.builder()
                .houseWorkId(uuidUtil.create())
                .title(createHouseWork.getTitle())
                .difficulty(createHouseWork.getDifficulty())
                .isCycle(createHouseWork.getIsCycle())
                .today(createHouseWork.getToday())
                .houseWorkStatusType(DEFAULT)
                .build();
    }

    @Transactional
    public String completion(String houseWorkId, HouseWorkStatusType houseWorkStatusType) {
        Member currentMemberObject = currentMemberUtil.getCurrentMemberObject();
        HouseWork findByHouseWorkId = houseWorkRepository.searchHouseWorkIdJoinManger(houseWorkId)
                .orElseThrow(() -> new DataNotFoundException(집안일을_찾을_수_없습니다));

        if (!currentMemberObject.getMemberEmail().equals(findByHouseWorkId.getManager().getMemberEmail())) { // 현재 로그인된 사용자가 집안일을 등록한 자이면 완료 표시 허용
            throw new IllegalStateException("권한 없음");
        }

        findByHouseWorkId.setCompleted(houseWorkStatusType);
        return findByHouseWorkId.getHouseWorkId();
    }
}
