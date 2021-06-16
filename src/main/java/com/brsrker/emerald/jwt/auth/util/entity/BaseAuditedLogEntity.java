package com.brsrker.emerald.jwt.auth.util.entity;

import com.brsrker.emerald.jwt.auth.config.CustomAuditingEntityListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, CustomAuditingEntityListener.class})
public abstract class BaseAuditedLogEntity<U> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "created_by")
    @CreatedBy
    private U createdBy;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_by")
    @LastModifiedBy
    private U updatedBy;
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
