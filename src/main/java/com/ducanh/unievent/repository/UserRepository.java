package com.ducanh.unievent.repository;

import com.ducanh.unievent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Optional<User> findByUsernameOrEmail(String username, String email);
}
