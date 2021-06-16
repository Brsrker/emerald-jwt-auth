package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.PrivilegeDTO;
import com.brsrker.emerald.jwt.auth.repository.PrivilegeRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PrivilegeServiceTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private final PrivilegeService privilegeService = new PrivilegeService();

    @BeforeEach
    void testCreate_whenRoleAlreadyExist_thenThrowsAlreadyExistException() {
        Privilege privilegeManager = new Privilege();
        privilegeManager.setName("EMERALD_MANAGEMENT");
        when(privilegeRepository.findOneByName("EMERALD_MANAGEMENT")).thenReturn(Optional.of(privilegeManager));

        Privilege privilegeUser = new Privilege();
        privilegeUser.setName("EMERALD_USER");
        when(privilegeRepository.findOneByName("EMERALD_USER")).thenReturn(Optional.of(privilegeUser));


        List<Privilege> elements = new ArrayList<>();
        elements.add(privilegeManager);
        elements.add(privilegeUser);
        Page<Privilege> pages = new PageImpl<>(elements);
        when(privilegeRepository.findAll(PageRequest.of(1,10))).thenReturn(pages);
    }

    @Test
    public void testCreate_whenPrivilegeAlreadyExist_thenThrowsAlreadyExistException() {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("EMERALD_MANAGEMENT");

        assertThrows(AlreadyExistException.class, () -> privilegeService.create(privilegeDTO));
    }

    @Test
    public void testCreate_whenPrivilegeNotExist_thenPrivilegeDTO() {
        PrivilegeDTO privilegeDTOToCreate = new PrivilegeDTO();
        privilegeDTOToCreate.setName("NEW_PRIVILEGE");

        PrivilegeDTO privilegeDTOCreated = privilegeService.create(privilegeDTOToCreate);

        assertNotNull(privilegeDTOCreated);
        assertEquals(privilegeDTOCreated.getName(), "NEW_PRIVILEGE");
    }

    @Test
    public void testList_whenPrivilegeExist_thenPrivilegeDTO() {

        Pageable pageable = PageRequest.of(1,10);

        ListResultDTO<PrivilegeDTO> list = privilegeService.list(pageable);

        assertNotNull(list);
        assertEquals(2, list.getCount());
        assertEquals(1, list.getPages());
        assertEquals(2, list.getList().size());
    }

    @Test
    public void testFindOne_whenPrivilegeExist_thenPrivilegeDTO() {
        PrivilegeDTO privilegeDTOFound = privilegeService.findOne("EMERALD_MANAGEMENT");

        assertNotNull(privilegeDTOFound);
        assertEquals(privilegeDTOFound.getName(), "EMERALD_MANAGEMENT");
    }

    @Test
    public void testFindOne_whenPrivilegeAndNotExist_thenThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> privilegeService.findOne("NON_EXIST_PRIVILEGE"));
    }

    @Test
    public void testGetPrivilegesByName_whenPrivilegesExist_thenSetPrivileges() {
        Set<String> privilegesNames = new HashSet<>();
        privilegesNames.add("EMERALD_MANAGEMENT");
        privilegesNames.add("EMERALD_USER");

        Set<Privilege> privileges = privilegeService.getPrivilegesByName(privilegesNames);

        assertNotNull(privileges);
        assertEquals(2, privileges.size());
    }

    @Test
    public void testUpdatePrivilegesByName_whenPrivilegesExist_thenPrivilegeDTO() {
        String name = "EMERALD_MANAGEMENT";
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("UPDATE");

        PrivilegeDTO privilegeUpdate = privilegeService.update(name, privilegeDTO);

        assertNotNull(privilegeUpdate);
        assertEquals(privilegeUpdate.getName(), "UPDATE");
    }

    @Test
    public void testUpdatePrivilegesByName_whenPrivilegesNotExist_thenThrowsNotFoundException() {
        String name = "NOT_EXIST";
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setName("NOT_EXIST");

        assertThrows(NotFoundException.class, () -> privilegeService.update(name, privilegeDTO));
    }

    @Test
    public void testDeletePrivilegesByName_whenPrivilegesNotExist_thenThrowsNotFoundException() {
        String name = "NOT_EXIST";
        assertThrows(NotFoundException.class, () -> privilegeService.deleteOne(name));
    }

}
