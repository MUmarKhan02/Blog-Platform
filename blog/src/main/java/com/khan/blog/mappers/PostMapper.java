package com.khan.blog.mappers;

import com.khan.blog.domain.CreatePostRequest;
import com.khan.blog.domain.UpdatePostRequest;
import com.khan.blog.domain.dtos.CreatePostRequestDto;
import com.khan.blog.domain.dtos.PostDto;
import com.khan.blog.domain.dtos.UpdatePostRequestDto;
import com.khan.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

//--------------------Mapper Interface for Mapping and Conversion Between Entity and DTO Objects for Post, as well as Their Requests--------------------\\

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target="author",source = "author")
    @Mapping(target="category",source = "category")
    @Mapping(target="tags",source = "tags")
    @Mapping(target = "status", source = "status")
    PostDto toDto(Post post);
    CreatePostRequest toCreatePostRequest(CreatePostRequestDto dto);
    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto dto);
}
