package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.entity.User;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.UserDTO;
import com.brsrker.emerald.jwt.auth.model.UserRegisterDTO;
import com.brsrker.emerald.jwt.auth.repository.UserRepository;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @InjectMocks
    private final UserService userService = new UserService();

    @BeforeEach
    void setMockOutput() {
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        
        User userManager = new User();
        userManager.setLoginId("USER_LOGIN_ID");
        when(userRepository.findByLoginId("USER_LOGIN_ID")).thenReturn(Optional.of(userManager));


        List<User> elements = new ArrayList<>();
        elements.add(userManager);
        Page<User> pages = new PageImpl<>(elements);
        when(userRepository.findAll(PageRequest.of(1,10))).thenReturn(pages);

        when(bcryptEncoder.encode("NEW_USER_PASSWORD")).thenReturn("NEW_USER_PASSWORD_HASH");
    }

    @Test
    public void testCreate_whenUserAlreadyExist_thenThrowsAlreadyExistException() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("USER_LOGIN_ID");

        assertThrows(AlreadyExistException.class, () -> userService.create(userRegisterDTO));
    }

    @Test
    public void testCreate_whenUserNotExist_thenUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setEmail("NEW_USER_LOGIN_ID");
        userRegisterDTO.setPassword("NEW_USER_PASSWORD");

        UserDTO userDTO = userService.create(userRegisterDTO);

        assertNotNull(userDTO);
        assertEquals(false, userDTO.getExpire());
        assertEquals(false, userDTO.getLocked());
        assertEquals(true, userDTO.getEnabled());
    }

    @Test
    public void testList_whenUserExist_thenUserDTO() {

        Pageable pageable = PageRequest.of(1,10);

        ListResultDTO<UserDTO> list = userService.list(pageable);

        assertNotNull(list);
        assertEquals(1, list.getCount());
        assertEquals(1, list.getPages());
        assertEquals(1, list.getList().size());
    }

    @Test
    public void testFindOne_whenUserExist_thenUserDTO() {
        UserDTO userDTOFound = userService.findOne("USER_LOGIN_ID");

        assertNotNull(userDTOFound);
        assertEquals(userDTOFound.getLoginId(), "USER_LOGIN_ID");
    }

    @Test
    public void testFindOne_whenUserAndNotExist_thenThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> userService.findOne("NON_EXIST_USER"));
    }

    @Test
    public void testDeleteUsersByName_whenUsersNotExist_thenThrowsNotFoundException() {
        String name = "NOT_EXIST";
        assertThrows(NotFoundException.class, () -> userService.deleteOne(name));
    }

}
