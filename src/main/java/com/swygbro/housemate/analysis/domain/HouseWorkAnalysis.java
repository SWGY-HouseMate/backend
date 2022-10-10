package com.swygbro.housemate.analysis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ziphap_house_work_analysis")
public class HouseWorkAnalysis {

    @Id
    String analysisId;

    String name;
}
