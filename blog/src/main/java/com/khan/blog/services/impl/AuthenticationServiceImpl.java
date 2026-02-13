package com.khan.blog.services.impl;

import com.khan.blog.domain.dtos.AuthResponse;
import com.khan.blog.domain.entities.RefreshToken;
import com.khan.blog.domain.entities.User;
import com.khan.blog.repositories.RefreshTokenRepository;
import com.khan.blog.repositories.UserRepository;
import com.khan.blog.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//--------------------Implementation for Authentication Services--------------------\\

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 86400000L;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final Long refreshTokenExpiryMs = 7*24*60*60*1000L;

    //--------------------Function for User Authentication with Email and Password--------------------\\

    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        return userDetailsService.loadUserByUsername(email);
    }

    //--------------------Function for Token Generation--------------------\\

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    //--------------------Function for Token Validation--------------------\\

    @Override
    public UserDetails validateToken(String token) {
        try {
            String username = extractUsername(token);
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }

    //--------------------Function for Refresh Token Generation--------------------\\

    @Override
    @Transactional
    public String generateRefreshToken(UserDetails userDetails) {
       User user = userRepository.findByEmail(userDetails.getUsername())
               .orElseThrow(()-> new IllegalStateException("User not found"));
       String token = java.util.UUID.randomUUID().toString();
       refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiryMs/1000))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    //--------------------Function for Refresh Token Validation--------------------\\

    @Override
    public UserDetails validateRefreshToken(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new IllegalStateException("Refresh token revoked");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new IllegalStateException("Refresh token expired");
        }

        return userDetailsService.loadUserByUsername(token.getUser().getEmail());
    }

    //--------------------Function for Automatically Creating a Refresh Token if Expired (To stay Logged In without Force Re-Login)--------------------\\

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {


        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));


        if (tokenEntity.isRevoked() || tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Refresh token expired or revoked");
        }


        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);


        User user = tokenEntity.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());


        String newAccessToken = generateToken(userDetails);


        String newRefreshToken = generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtExpiryMs / 1000)
                .build();
    }

    //--------------------Function for Extracting a Username--------------------\\


    private String extractUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    //--------------------Function for Getting the Sign in Key--------------------\\

    private Key getSigningKey(){
       byte[] keyBytes= secretKey.getBytes();
       return Keys.hmacShaKeyFor(keyBytes);
    }
}
