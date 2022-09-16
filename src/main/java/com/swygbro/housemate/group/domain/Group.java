package com.swygbro.housemate.group.domain;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "groups")
public class Group extends AbstractEntity {

    @Id
    private String groupId;

    private String link;

    private String name;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member owner;

    // 연관관계 편의 메소드
    public void applyMember(final Member member) {
        this.owner = member;
        member.setGroup(this);
    }
}
