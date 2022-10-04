package com.swygbro.housemate.heart.domain;

import com.swygbro.housemate.util.model.AbstractEntity;
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
@Table(name = "ziphap_heart")
public class Heart extends AbstractEntity {

    @Id
    private String heartId;

    @Enumerated(STRING)
    private HeartType kind;

    private String title;

    private Boolean isCreateAllMembers;
}
