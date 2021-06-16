package com.brsrker.emerald.jwt.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLogDTO {
    private String entity;
    private String entityID;
    private String action;
    private String username;
    private LocalDateTime timestamp;
    private String value;
}
