package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.RoleDTO;
import com.brsrker.emerald.jwt.auth.entity.Role;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import com.brsrker.emerald.jwt.auth.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeService privilegeService;

    public RoleDTO create(RoleDTO roleDTO) {

        if(roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new AlreadyExistException("The role " + roleDTO.getName() + " already exist!");
        }

        Set<Privilege> privileges = privilegeService.getPrivilegesByName(roleDTO.getPrivileges());

        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return new ModelMapper().map(role, RoleDTO.class);
    }

    public ListResultDTO<RoleDTO> list(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);

        ModelMapper modelMapper = new ModelMapper();
        List<RoleDTO> rolesDTOS = roles.stream()
                .map(user -> modelMapper.map(user, RoleDTO.class))
                .collect(Collectors.toList());

        ListResultDTO<RoleDTO> listResultDTO = new ListResultDTO<>();
        listResultDTO.setPages(roles.getTotalPages());
        listResultDTO.setCount(roles.getTotalElements());
        listResultDTO.setList(rolesDTOS);

        return listResultDTO;
    }

    public RoleDTO findOne(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("The Role " + name + " not exist!"));
        return new ModelMapper().map(role, RoleDTO.class);
    }

    public RoleDTO update(String name, RoleDTO roleDTO) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("The Role " + roleDTO.getName() + " not exist!"));

        Set<Privilege> privileges = privilegeService.getPrivilegesByName(roleDTO.getPrivileges());

        role.setName(roleDTO.getName());
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return new ModelMapper().map(role, RoleDTO.class);
    }

    public void deleteOne(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("The Role " + name + " not exist!"));
        for (Privilege privilege: role.getPrivileges()){
            role.removePrivilege(privilege);
        }
        roleRepository.delete(role);
    }
}
