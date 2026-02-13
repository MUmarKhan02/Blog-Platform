package com.khan.blog.mappers;
import java.util.List;

import com.khan.blog.domain.dtos.CreateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.khan.blog.domain.PostStatus;
import com.khan.blog.domain.dtos.CategoryDto;
import com.khan.blog.domain.entities.Category;
import com.khan.blog.domain.entities.Post;

//--------------------Mapper Interface for Mapping and Conversion Between Entity and DTO Objects for Category--------------------\\

@Mapper(componentModel = "spring",unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount",source = "posts",qualifiedByName="calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);


    @Named("calculatePostCount")
    default long calculatePostCount(List<Post>posts){
        if (null == posts) {
            return 0;
        }
        return posts.stream().filter(post->PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}
