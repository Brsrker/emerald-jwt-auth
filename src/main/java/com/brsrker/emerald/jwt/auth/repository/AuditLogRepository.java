package com.brsrker.emerald.jwt.auth.repository;

import com.brsrker.emerald.jwt.auth.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, Integer> {
    Page<AuditLog> findByEntity(String entity, Pageable pageable);
    Page<AuditLog> findAll(Pageable pageable);
}
