package com.brsrker.emerald.jwt.auth.repository;

import com.brsrker.emerald.jwt.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Page<User> findAll(Pageable pageable);
    Optional<User> findByLoginId(String loginId);
}
