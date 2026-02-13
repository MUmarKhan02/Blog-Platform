package com.khan.blog.domain.dtos;

import lombok.Data;

//--------------------Dto for the Request for Refresh Token--------------------\\

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
