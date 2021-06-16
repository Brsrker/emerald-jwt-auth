package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.entity.User;
import com.brsrker.emerald.jwt.auth.repository.PrivilegeRepository;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
import com.brsrker.emerald.jwt.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PrivilegeRepository privilegeRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setMockOutput() {

        Privilege privilegeManager = new Privilege();
        privilegeManager.setName("EMERALD_MANAGEMENT");
        when(privilegeRepository.findOneByName("EMERALD_MANAGEMENT")).thenReturn(Optional.of(privilegeManager));

        Set<Privilege> privileges = new HashSet<>();
        privileges.add(privilegeManager);

        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleUser.setPrivileges(privileges);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        User userManager = new User();
        userManager.setLoginId("USER_LOGIN_ID");
        userManager.setRoles(roles);
        userManager.setExpire(false);
        userManager.setEnabled(true);
        userManager.setLocked(false);
        userManager.setPassword("USER_PASSWORD");

        when(userRepository.findByLoginId("USER_LOGIN_ID")).thenReturn(Optional.of(userManager));
    }

    @Test
    public void testLoadUserByUsername_whenLoginIdExist_thenNotNull() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("USER_LOGIN_ID");
        assertNotNull(userDetails);
        assertEquals("USER_LOGIN_ID", userDetails.getUsername());
    }
}
