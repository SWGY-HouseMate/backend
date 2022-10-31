package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.housework.domain.Cycle;
import com.swygbro.housemate.housework.domain.DifficultyType;
import com.swygbro.housemate.housework.domain.HouseWorkStatusType;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkById {

    private String houseWorkId;

    private String title;

    private DifficultyType difficulty;

    private MemberInfo memberInfo;

    private String today;

    private HouseWorkStatusType houseWorkStatusType;

    private GroupInfo groupInfo;

    private Boolean isCycle;

    private CycleInfo cycleInfo;
}
