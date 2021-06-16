package com.brsrker.emerald.jwt.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String entity;
    @Column
    private long entityID;
    @Column
    private String action;
    @Column
    @CreatedBy
    private String username;
    @Column(name = "timestamp", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime timestamp;
    @Column(name = "value", columnDefinition="TEXT")
    private String objectValue;

}
