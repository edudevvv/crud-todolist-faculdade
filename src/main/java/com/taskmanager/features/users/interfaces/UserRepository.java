package com.taskmanager.features.users.interfaces;

import com.taskmanager.features.users.respository.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
