package com.swygbro.housemate.analysis.domain;

import com.swygbro.housemate.analysis.message.ratio.ShareRatioType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
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

    // ====================== 공통

    String memberId;

    String groupId;

    LocalDate today;

    // ====================== 분담 비율

    @Enumerated(STRING)
    ShareRatioType shareRatioType;

    double shareRatioPercent;

    // ====================== 가장 많이 한 집안일

    String mostTitle;

    Integer count;

    // ====================== 가장 잘한 집안일 / 담당자 변경이 필요한 집안일

    String bestHouseWorkId;

    String bestHouseWorkTitle;

    String bestHouseWorkManager;

    String worstHouseWorkId;

    String worstHouseWorkTitle;

    String worstHouseWorkManager;
}
