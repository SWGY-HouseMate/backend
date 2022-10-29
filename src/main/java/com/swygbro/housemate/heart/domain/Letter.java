package com.swygbro.housemate.heart.domain;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ziphap_letter")
public class Letter extends AbstractEntity {

    @Id
    private String letterId;

    private String title; // TODO : 지우기

    private String content;

    @Enumerated(STRING)
    private HeartType heartType;

    private String letterFrom;

    private String letterTo;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "heartId")
    private Heart heart;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "zipHapGroupId")
    private Group group;
}
