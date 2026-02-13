package com.khan.blog.services;

import com.khan.blog.domain.dtos.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

//--------------------Interface for Authentication Services--------------------\\

public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);

    String generateRefreshToken(UserDetails userDetails);
    UserDetails validateRefreshToken(String refreshToken);
    AuthResponse refreshAccessToken(String refreshToken);
}
