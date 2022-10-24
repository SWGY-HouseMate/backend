package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.AbstractEntity;
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

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cycle")
public class Cycle extends AbstractEntity {

    @Id
    private String cycleId;

    @Enumerated(STRING)
    private CycleType cycleType;

    private String props;

    private LocalDate startAt;

    private LocalDate endAt;

}
