package com.khan.blog.services;


import com.khan.blog.domain.dtos.RegisterRequest;
import com.khan.blog.domain.dtos.UserProfileDto;
import com.khan.blog.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

//--------------------Interface for User Services--------------------\\

public interface UserService {
    User getUserById(UUID id);
    UserProfileDto getCurrentUserProfile(UserDetails userDetails);
    User register(RegisterRequest request);
}
