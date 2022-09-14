package com.swygbro.housemate.util.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class AbstractEntity {

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Getter
    @Setter
    private LocalDateTime updatedAt;

    public String createAt() {
        return getStringDate(this.createdAt);
    }

    public String updateAt() {
        return getStringDate(this.updatedAt);
    }

    private String getStringDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getStringDateyyyyMMdd(LocalDateTime localDateTime) {
        return localDateTime != null? localDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) :  null;
    }

    public int getUpdatedYear() {
        return updatedAt.getYear();
    }

}
