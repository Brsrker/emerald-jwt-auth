package com.brsrker.emerald.jwt.auth.repository;

import com.brsrker.emerald.jwt.auth.entity.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Integer> {
    Page<Privilege> findAll(Pageable pageable);
    Optional<Privilege> findOneByName(String name);
}
