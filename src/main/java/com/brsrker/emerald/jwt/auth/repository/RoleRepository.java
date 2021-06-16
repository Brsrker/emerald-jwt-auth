package com.brsrker.emerald.jwt.auth.repository;

import com.brsrker.emerald.jwt.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Page<Role> findAll(Pageable pageable);
    Optional<Role> findByName(String name);
}
