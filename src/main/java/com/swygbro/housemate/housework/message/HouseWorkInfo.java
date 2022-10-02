package com.swygbro.housemate.housework.message;

import com.swygbro.housemate.housework.domain.DifficultyType;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Getter
@Value(staticConstructor = "of")
public class HouseWorkInfo {

    String houseWorkId;
    String title;
    DifficultyType difficultyType;
    LocalDate today;
    Boolean isCompleted;
    Boolean isCycle;
    CycleInfo cycleInfo;
    MemberInfo memberInfo;

}
