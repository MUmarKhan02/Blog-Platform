package com.khan.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//--------------------Dto for the Author--------------------\\

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    private UUID id;
    private String name;

}
