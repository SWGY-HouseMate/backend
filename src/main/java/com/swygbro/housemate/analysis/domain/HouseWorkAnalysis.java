package com.swygbro.housemate.analysis.domain;

import com.swygbro.housemate.analysis.message.ShareRatioType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ziphap_house_work_analysis")
public class HouseWorkAnalysis {
    @Id
    String analysisId;

    String memberId;

    String groupId;

    @Enumerated(STRING)
    ShareRatioType shareRatioType;

    double shareRatioPercent;

    LocalDate today;
}
