package com.brsrker.emerald.jwt.auth.service;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import com.brsrker.emerald.jwt.auth.exception.AlreadyExistException;
import com.brsrker.emerald.jwt.auth.exception.NotFoundException;
import com.brsrker.emerald.jwt.auth.model.PrivilegeDTO;
import com.brsrker.emerald.jwt.auth.repository.PrivilegeRepository;
import com.brsrker.emerald.jwt.auth.util.model.ListResultDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    public PrivilegeDTO create(PrivilegeDTO privilegeDTO) {

        if(privilegeRepository.findOneByName(privilegeDTO.getName()).isPresent()) {
            throw new AlreadyExistException("The privilege " + privilegeDTO.getName() + " already exist!");
        }

        Privilege privilege = new Privilege();
        privilege.setName(privilegeDTO.getName());
        privilegeRepository.save(privilege);
        return parseToDTO(privilege);
    }

    public ListResultDTO<PrivilegeDTO> list(Pageable pageable) {
        Page<Privilege> privileges = privilegeRepository.findAll(pageable);

        List<PrivilegeDTO> privilegesDTOS = privileges.stream()
                .map(this::parseToDTO)
                .collect(Collectors.toList());

        ListResultDTO<PrivilegeDTO> listResultDTO = new ListResultDTO<>();
        listResultDTO.setPages(privileges.getTotalPages());
        listResultDTO.setCount(privileges.getTotalElements());
        listResultDTO.setList(privilegesDTOS);

        return listResultDTO;
    }

    public Set<Privilege> getPrivilegesByName(Set<String> privileges) {
        return privileges.stream()
                .map(privilege -> privilegeRepository.findOneByName(privilege))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public PrivilegeDTO findOne(String name) {
        Privilege privilege = findOneByName(name);
        return parseToDTO(privilege);
    }

    @Transactional
    public PrivilegeDTO update(String name, PrivilegeDTO privilegeDTO) {
        Privilege privilege = findOneByName(name);
        privilege.setName(privilegeDTO.getName());
        return parseToDTO(privilege);
    }

    public void deleteOne(String name) {
        Privilege privilege = findOneByName(name);
        privilegeRepository.delete(privilege);
    }

    private PrivilegeDTO parseToDTO(Privilege privilege) {
        return new ModelMapper().map(privilege, PrivilegeDTO.class);
    }

    public Privilege findOneByName(String name) {
        return privilegeRepository.findOneByName(name)
                .orElseThrow(() -> new NotFoundException("The User " + name + " not exist!"));
    }
}
