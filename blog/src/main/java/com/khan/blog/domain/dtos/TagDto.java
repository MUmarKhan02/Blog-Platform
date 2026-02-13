package com.khan.blog.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//--------------------Dto for the Tag--------------------\\

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto {
    private UUID id;
    private String name;
    private Integer postCount;
}
