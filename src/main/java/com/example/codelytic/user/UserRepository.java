package com.example.codelytic.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
