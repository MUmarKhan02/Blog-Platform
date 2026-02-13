package com.khan.blog.controllers;

import com.khan.blog.domain.dtos.AuthResponse;
import com.khan.blog.domain.dtos.LoginRequest;
import com.khan.blog.domain.dtos.RefreshTokenRequest;
import com.khan.blog.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//--------------------Controller For Authorization Controls--------------------\\

@RestController
@RequestMapping(path="/api/v1/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthenticationService authenticationService;

    //--------------------Managing and Mapping Login Function and Navigate Login Page--------------------\\

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authenticationService.authenticate(loginRequest.getEmail(),loginRequest.getPassword());
        String tokenValue= authenticationService.generateToken(userDetails);
        String refreshToken = authenticationService.generateRefreshToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder().token(tokenValue).refreshToken(refreshToken).expiresIn(86400).build();
        return ResponseEntity.ok(authResponse);
    }

    //--------------------Managing and Mapping Refresh Function and Navigate Refresh Page--------------------\\

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request){
        AuthResponse response = authenticationService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }


}
