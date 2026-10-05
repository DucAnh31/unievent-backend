package com.ducanh.unievent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ducanh.unievent.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public Optional<User> findByUsername(String username);

    public Optional<User> findByUsernameOrEmail(String username, String email);

    public Boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);
}
