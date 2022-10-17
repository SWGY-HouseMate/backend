package com.swygbro.housemate.analysis.message.result;

import com.swygbro.housemate.analysis.message.ratio.ShareRatioType;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.login.message.MemberInfo;
import lombok.ToString;
import lombok.Value;

import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@ToString
@Value(staticConstructor = "of")
public class AnalysisComplete {
    String analysisId;

    MemberInfo memberInfo;

    GroupInfo groupInfo;

    LocalDate today;

    @Enumerated(STRING)
    ShareRatioType shareRatioType;

    double shareRatioPercent;

    String mostTitle;

    Integer count;

    String bestHouseWorkId;

    String worstHouseWorkId;

    LocalDateTime startAt;

    LocalDateTime endAt;
}
