package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

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
