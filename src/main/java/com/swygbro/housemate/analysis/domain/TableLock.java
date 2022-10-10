package com.swygbro.housemate.analysis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swygbro.housemate.util.analysis.BooleanToYNConverter;
import com.swygbro.housemate.util.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "batch_lock")
public class TableLock extends AbstractEntity {

    @Id
    private String instanceId;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime checkDataTime;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean useYn;

    public void setUseYn(boolean useYn) {
        this.useYn = useYn;
    }

    public void setCheckDataTime(LocalDateTime now) {
        this.checkDataTime = now;
    }
}
