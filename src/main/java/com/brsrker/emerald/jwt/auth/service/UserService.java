package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.entity.User;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.UserDTO;
import com.brsrker.emerald.jwt.auth.model.UserRegisterDTO;
import com.brsrker.emerald.jwt.auth.repository.UserRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public UserDTO create(UserRegisterDTO userDTO) {

        if(userRepository.findByLoginId(userDTO.getEmail()).isPresent()){
            throw new AlreadyExistException("The user " + userDTO.getEmail() + " already exist!");
        }

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotFoundException("Role USER not found"));

        User user = new User();
        user.setLoginId(userDTO.getEmail());
        user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
        user.setEnabled(userDTO.getEnabled() != null ? userDTO.getEnabled() : true);
        user.setLocked(userDTO.getLocked() != null ? userDTO.getLocked() : false);

        boolean userExpire = userDTO.getExpire() != null ? userDTO.getExpire() : false;
        user.setExpire(userExpire);
        if(userExpire && userDTO.getExpireAt() != null) {
            user.setExpireAt(userDTO.getExpireAt());
        } else {
            user.setExpireAt(LocalDateTime.now());
        }
        user.addRole(role);

        userRepository.save(user);
        return parseToDTO(user);
    }

    public UserDTO findOne(String loginId) {
        return parseToDTO(findByLoginId(loginId));
    }

    public ListResultDTO<UserDTO> list(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        List<UserDTO> userDTOS = users.stream()
                .map(this::parseToDTO)
                .collect(Collectors.toList());

        ListResultDTO<UserDTO> listResultDTO = new ListResultDTO<>();
        listResultDTO.setPages(users.getTotalPages());
        listResultDTO.setCount(users.getTotalElements());
        listResultDTO.setList(userDTOS);
        return listResultDTO;
    }

    public void deleteOne(String loginId) {
        User user = findByLoginId(loginId);
        for (Role role: user.getRoles()){
            user.removeRole(role);
        }
        userRepository.delete(user);
    }

    private UserDTO parseToDTO(User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }

    @Transactional
    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("The User " + loginId + " not exist!"));
    }

}
