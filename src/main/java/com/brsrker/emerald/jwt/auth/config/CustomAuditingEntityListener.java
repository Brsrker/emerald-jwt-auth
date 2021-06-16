package com.brsrker.emerald.jwt.auth.config;

import com.brsrker.emerald.jwt.auth.entity.AuditLog;
import com.brsrker.emerald.jwt.auth.util.service.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class CustomAuditingEntityListener extends AuditingEntityListener {

    private final static String CREATE = "CREATE";
    private final static String UPDATE = "UPDATE";
    private final static String DELETE = "DELETE";

    @PrePersist
    private void preCreate(Object object){
        perform(CREATE, object);
    }
    @PreUpdate
    private void preUpdate(Object object){
        perform(UPDATE, object);
    }
    @PreRemove
    private void preDelete(Object object){
        perform(DELETE, object);
    }

    private void perform(String action, Object values){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        AuditLog auditLog = new AuditLog();
        auditLog.setEntity(values.getClass().getCanonicalName());
        auditLog.setAction(action);
        try {
            auditLog.setObjectValue(mapper.writeValueAsString(values));
        } catch (JsonProcessingException e) {
            auditLog.setObjectValue("Unable to process entity");
        }
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(auditLog);
    }

}
