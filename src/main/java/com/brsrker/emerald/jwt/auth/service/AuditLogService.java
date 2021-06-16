package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.AuditLog;
import com.brsrker.emerald.jwt.auth.model.AuditLogDTO;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import com.brsrker.emerald.jwt.auth.repository.AuditLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public ListResultDTO<AuditLogDTO> list(String entity, Pageable pageable) {
        Page<AuditLog> auditLogs = auditLogRepository.findByEntity(entity, pageable);
        return map(auditLogs);
    }

    public ListResultDTO<AuditLogDTO> list(Pageable pageable) {
        Page<AuditLog> auditLogs = auditLogRepository.findAll(pageable);
        return map(auditLogs);
    }

    private ListResultDTO<AuditLogDTO> map(Page<AuditLog> auditLogs){
        ModelMapper modelMapper = new ModelMapper();
        List<AuditLogDTO> auditLogDTOS = auditLogs.stream()
                .map(user -> modelMapper.map(user, AuditLogDTO.class))
                .collect(Collectors.toList());

        ListResultDTO<AuditLogDTO> listResultDTO = new ListResultDTO<>();
        listResultDTO.setPages(auditLogs.getTotalPages());
        listResultDTO.setCount(auditLogs.getTotalElements());
        listResultDTO.setList(auditLogDTOS);
        return listResultDTO;
    }

}
