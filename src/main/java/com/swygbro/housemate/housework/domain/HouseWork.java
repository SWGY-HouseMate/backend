package com.swygbro.housemate.housework.domain;

import com.swygbro.housemate.group.domain.Group;
import com.swygbro.housemate.login.domain.Member;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor_ = @PersistenceConstructor)
@Table(name = "house_work")
public class HouseWork extends AbstractEntity {

    @Id
    private String houseWorkId;

    private String title;

    @Enumerated(STRING)
    private DifficultyType difficulty;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "memberId")
    private Member manager;

    private LocalDate today;

    private Boolean isCompleted;

    private Boolean isCycle;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "zipHapGroupId")
    private Group group;

    @ManyToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "cycleId")
    private Cycle cycle;

    public void setAssign(Member manager, Group group) {
        this.manager = manager;
        this.group = group;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }
}
