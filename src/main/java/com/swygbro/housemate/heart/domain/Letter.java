package com.swygbro.housemate.heart.domain;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ziphap_letter")
public class Letter extends AbstractEntity {

    @Id
    private String letterId;

    private String title;

    private String content;

    @Enumerated(STRING)
    private HeartType kind;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member from;

    @OneToOne
    @JoinColumn(name = "heartId")
    private Heart heart;

    @OneToOne
    @JoinColumn(name = "groupId")
    private Group group;
}
