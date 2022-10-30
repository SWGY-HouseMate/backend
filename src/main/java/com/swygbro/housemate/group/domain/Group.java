package com.swygbro.housemate.group.domain;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "ziphap_group")
public class Group extends AbstractEntity {

    @Id
    private String zipHapGroupId;

    private String linkId;

    private String groupName;

    private Integer participatingMembers;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member owner;

    public void applyMember(final Member member) {
        this.owner = member;
        member.setZipHapGroup(this);
    }

    public void updateParticipatingMembers() {
        this.participatingMembers = this.participatingMembers + 1;
    }
}
