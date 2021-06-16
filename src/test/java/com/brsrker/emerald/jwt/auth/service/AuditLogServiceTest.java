package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.AuditLog;
import com.brsrker.emerald.jwt.auth.model.AuditLogDTO;
import com.brsrker.emerald.jwt.auth.repository.AuditLogRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private final AuditLogService auditLogService = new AuditLogService();

    @BeforeEach
    void setMockOutput() {

        AuditLog auditLog1 = new AuditLog();
        auditLog1.setAction("action");
        auditLog1.setEntity("entityA");
        auditLog1.setTimestamp(LocalDateTime.now());
        auditLog1.setObjectValue("object");

        AuditLog auditLog2 = new AuditLog();
        auditLog2.setAction("action");
        auditLog2.setEntity("entityB");
        auditLog2.setTimestamp(LocalDateTime.now());
        auditLog1.setObjectValue("object");

        List<AuditLog> allElements = new ArrayList<>();
        allElements.add(auditLog1);
        allElements.add(auditLog2);

        Page<AuditLog> allElementsPage = new PageImpl<>(allElements);

        when(auditLogRepository.findAll(PageRequest.of(1,10))).thenReturn(allElementsPage);

        List<AuditLog> elementsFromA = new ArrayList<>();
        elementsFromA.add(auditLog1);

        Page<AuditLog> elementsFromAPage = new PageImpl<>(elementsFromA);

        when(auditLogRepository.findByEntity("entityA", PageRequest.of(1,10))).thenReturn(elementsFromAPage);
    }


    @Test
    public void testList_thenPage() {

        Pageable pageable = PageRequest.of(1,10);

        ListResultDTO<AuditLogDTO> list = auditLogService.list(pageable);

        assertNotNull(list);
        assertEquals(2, list.getCount());
        assertEquals(1, list.getPages());
        assertEquals(2, list.getList().size());
    }

    @Test
    public void testList_withEntityName_thenPage() {

        Pageable pageable = PageRequest.of(1,10);

        ListResultDTO<AuditLogDTO> list = auditLogService.list("entityA", pageable);

        assertNotNull(list);
        assertEquals(1, list.getCount());
        assertEquals(1, list.getPages());
        assertEquals(1, list.getList().size());
    }
}
