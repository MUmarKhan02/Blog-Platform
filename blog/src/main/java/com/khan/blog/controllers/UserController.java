package com.khan.blog.controllers;


import com.khan.blog.domain.dtos.RegisterRequest;
import com.khan.blog.domain.dtos.UserProfileDto;
import com.khan.blog.domain.entities.User;
import com.khan.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//--------------------Controller for Functions and Controls Regarding The Users and Profiles--------------------\\

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //--------------------Function and Mapping for Getting a User Profile--------------------\\

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.getCurrentUserProfile(userDetails));
    }

    //--------------------Function and Mapping for Registering a User--------------------\\

    @PostMapping("/register")
    public ResponseEntity<UserProfileDto> register(@RequestBody RegisterRequest request){
        User user = userService.register(request);
        UserProfileDto profile = UserProfileDto.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(profile);
    }
}
