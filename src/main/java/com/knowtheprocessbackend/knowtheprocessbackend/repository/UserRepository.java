package com.knowtheprocessbackend.knowtheprocessbackend.repository;
import com.knowtheprocessbackend.knowtheprocessbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

