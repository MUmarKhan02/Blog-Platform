package com.khan.blog.repositories;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khan.blog.domain.entities.User;

import org.springframework.stereotype.Repository;

//--------------------Repository Information Regarding Users--------------------\\

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}
