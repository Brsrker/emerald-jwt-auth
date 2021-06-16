package com.brsrker.emerald.jwt.auth.controller;

import com.brsrker.emerald.jwt.auth.model.AuditLogDTO;
import com.brsrker.emerald.jwt.auth.service.AuditLogService;
import com.brsrker.emerald.jwt.auth.security.IsEmeraldManager;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @IsEmeraldManager
    @GetMapping(value = "/audit_logs")
    public ResponseEntity<ListResultDTO<AuditLogDTO>> list(
            @RequestParam(name = "entity") Optional<String> entity,
            @RequestParam(name = "page") Optional<Integer> page,
            @RequestParam(name = "size") Optional<Integer> size,
            @RequestParam(name = "sort_by") Optional<String> sortBy) {

        Pageable pageable = PageRequest.of(
                Math.max(page.orElse(1), 1) - 1,
                Math.max(size.orElse(10), 1),
                Sort.by(sortBy.orElse("id")));

        ListResultDTO<AuditLogDTO> results = entity.map(
                entityName -> auditLogService.list(entityName, pageable))
                .orElseGet(() -> auditLogService.list(pageable));

        return ResponseEntity.ok(results);

    }

}
