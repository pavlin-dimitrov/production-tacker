package com.example.productiontracker.auditor;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@ToString
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

    @CreatedBy
    protected T createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected LocalDateTime createdAt;

    @LastModifiedBy
    protected T lastModifiedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected LocalDateTime lastModifiedAt;

    public Auditable(LocalDateTime createdAt, T createdBy, T lastModifiedBy, LocalDateTime lastModifiedAt) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedAt = lastModifiedAt;
    }
}
