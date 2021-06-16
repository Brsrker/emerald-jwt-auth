package com.brsrker.emerald.jwt.auth.controller;

import com.brsrker.emerald.jwt.auth.config.SpringSecurityWebAuxTestConfig;
import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.entity.User;
import com.brsrker.emerald.jwt.auth.model.UserDTO;
import com.brsrker.emerald.jwt.auth.model.UserRegisterDTO;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
import com.brsrker.emerald.jwt.auth.repository.UserRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SpringSecurityWebAuxTestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController controller;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

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
    public void contextLoads() {
        assertNotNull(controller);
    }

    @Test
    public void testRegister_whenAlreadyExist() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("USER_LOGIN_ID");
        userDTO.setPassword("USER_PASSWORD");

        String requestJson = objectMapper.writeValueAsString(userDTO);

        this.mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegister_whenInvalidPassWordRegexp() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("user@email.com");
        userDTO.setPassword("INVALID_PASSWORD");

        String requestJson = objectMapper.writeValueAsString(userDTO);

        this.mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegister_whenInvalidEmailRegexp() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("INVALID_EMAIL");
        userDTO.setPassword("qw12_.QW00!$");

        String requestJson = objectMapper.writeValueAsString(userDTO);
        this.mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegister_whenValidRegexp() throws Exception {
        UserRegisterDTO userDTO = new UserRegisterDTO();
        userDTO.setEmail("user@email.com");
        userDTO.setPassword("qw12_.QW00!$");

        String requestJson = objectMapper.writeValueAsString(userDTO);
        this.mockMvc.perform(post("/register").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("USER_ADMIN")
    public void testList_whenUserExist_thenListResult() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("USER_ADMIN")
    public void testFindOne_whenUserExist_thenUser() throws Exception {

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setLoginId("admin@brsrker.com");
        userDTO1.setEnabled(true);
        userDTO1.setLocked(false);
        userDTO1.setExpire(false);

        String exceptedResponse = objectMapper.writeValueAsString(userDTO1);
        this.mockMvc.perform(get("/users/admin@brsrker.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(exceptedResponse));
    }

    @Test
    @WithUserDetails("USER_ADMIN")
    public void testFindOne_whenUserNotExist_thenNotFound() throws Exception {
        this.mockMvc.perform(get("/users/not.exist@brsrker.com"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
