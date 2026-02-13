package com.khan.blog.domain.dtos;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//--------------------Dto for the Category--------------------\\

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto {
    private UUID id;
    private String name;
    private long postCount;
}
