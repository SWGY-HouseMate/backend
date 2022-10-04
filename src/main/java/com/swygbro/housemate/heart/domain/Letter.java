package com.swygbro.housemate.heart.domain;

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
@Table(name = "ziphap_letter")
public class Letter extends AbstractEntity {

    @Id
    private String letterId;

    private String content;

    @OneToOne
    @JoinColumn(name = "heartId")
    private Heart heart;
}
