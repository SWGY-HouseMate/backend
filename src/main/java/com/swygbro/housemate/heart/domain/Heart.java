package com.swygbro.housemate.heart.domain;

import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ziphap_heart")
public class Heart extends AbstractEntity {

    @Id
    private String heartId;

    private Boolean isCreateAllMembers;

    public void read() {
        this.isCreateAllMembers = true;
    }
}
