package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.RoleDTO;
import com.brsrker.emerald.jwt.auth.repository.PrivilegeRepository;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PrivilegeRepository privilegeRepository;
    @Mock
    private PrivilegeService privilegeService;

    @InjectMocks
    private final RoleService roleService = new RoleService();

    @BeforeEach
    void setMockOutput() {

        Privilege privilegeManager = new Privilege();
        privilegeManager.setName("EMERALD_MANAGEMENT");
        when(privilegeRepository.findOneByName("EMERALD_MANAGEMENT")).thenReturn(Optional.of(privilegeManager));

        Privilege privilegeUser = new Privilege();
        privilegeUser.setName("EMERALD_USER");
        when(privilegeRepository.findOneByName("EMERALD_USER")).thenReturn(Optional.of(privilegeUser));

        Set<Privilege> privileges = new HashSet<>();
        privileges.add(privilegeManager);
        privileges.add(privilegeUser);


        Set<String> privilegesNames = new HashSet<>();
        privilegesNames.add("EMERALD_MANAGEMENT");
        privilegesNames.add("EMERALD_USER");

        when(privilegeService.getPrivilegesByName(privilegesNames)).thenReturn(privileges);

        Role roleManager = new Role();
        roleManager.setName("ROLE_ADMIN");
        roleManager.setPrivileges(privileges);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleManager));

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleUser.setPrivileges(privileges);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));


        List<Role> elements = new ArrayList<>();
        elements.add(roleManager);
        elements.add(roleUser);
        Page<Role> pages = new PageImpl<>(elements);
        when(roleRepository.findAll(PageRequest.of(1,10))).thenReturn(pages);
    }

    @Test
    public void testCreate_whenRoleAlreadyExist_thenThrowsAlreadyExistException() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("ROLE_ADMIN");

        assertThrows(AlreadyExistException.class, () -> roleService.create(roleDTO));
    }

    @Test
    public void testCreate_whenRoleNotExist_thenRoleDTO() {
        RoleDTO roleDTOToCreate = new RoleDTO();
        roleDTOToCreate.setName("NEW_ROLE");

        RoleDTO roleDTOCreated = roleService.create(roleDTOToCreate);

        assertNotNull(roleDTOCreated);
        assertEquals(roleDTOCreated.getName(), "NEW_ROLE");
    }

    @Test
    public void testList_whenRoleExist_thenRoleDTO() {

        Pageable pageable = PageRequest.of(1,10);

        ListResultDTO<RoleDTO> list = roleService.list(pageable);

        assertNotNull(list);
        assertEquals(2, list.getCount());
        assertEquals(1, list.getPages());
        assertEquals(2, list.getList().size());
    }

    @Test
    public void testFindOne_whenRoleExist_thenRoleDTO() {
        RoleDTO roleDTOFound = roleService.findOne("ROLE_ADMIN");

        assertNotNull(roleDTOFound);
        assertEquals(roleDTOFound.getName(), "ROLE_ADMIN");
    }

    @Test
    public void testFindOne_whenRoleAndNotExist_thenThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> roleService.findOne("NON_EXIST_ROLE"));
    }

    @Test
    public void testUpdateRolesByName_whenRolesExist_thenRoleDTO() {
        String name = "ROLE_ADMIN";
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("UPDATE");

        RoleDTO roleUpdate = roleService.update(name, roleDTO);

        assertNotNull(roleUpdate);
        assertEquals(roleUpdate.getName(), "UPDATE");
    }

    @Test
    public void testUpdateRolesByName_whenRolesNotExist_thenThrowsNotFoundException() {
        String name = "NOT_EXIST";
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName("NOT_EXIST");

        assertThrows(NotFoundException.class, () -> roleService.update(name, roleDTO));
    }

    @Test
    public void testDeleteRolesByName_whenRolesNotExist_thenThrowsNotFoundException() {
        String name = "NOT_EXIST";
        assertThrows(NotFoundException.class, () -> roleService.deleteOne(name));
    }

}
