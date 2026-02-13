package com.khan.blog.repositories;

import com.khan.blog.domain.entities.RefreshToken;
import com.khan.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

//--------------------Repository Information Regarding Refresh Token--------------------\\

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

}
