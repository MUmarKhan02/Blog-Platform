package com.khan.blog.services.impl;

import com.khan.blog.domain.dtos.RegisterRequest;
import com.khan.blog.domain.dtos.UserProfileDto;
import com.khan.blog.domain.entities.User;
import com.khan.blog.repositories.UserRepository;
import com.khan.blog.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

//--------------------Implementation for User Services--------------------\\

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //--------------------Function for Getting a User by ID--------------------\\

    @Override
    public User getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found with id: " + id));

    }

    //--------------------Function for Getting the Current Logged In User Profile--------------------\\

    @Override
    public UserProfileDto getCurrentUserProfile(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(()-> new IllegalStateException("User not found"));
        return UserProfileDto.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    //--------------------Function for User Registration--------------------\\

    @Override
    public User register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent())throw new IllegalStateException("Email already in use");
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        return userRepository.save(user);
    }
}
