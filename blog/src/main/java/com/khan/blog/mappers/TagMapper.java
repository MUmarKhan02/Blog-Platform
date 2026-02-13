package com.khan.blog.mappers;

import com.khan.blog.domain.PostStatus;
import com.khan.blog.domain.dtos.TagDto;
import com.khan.blog.domain.entities.Post;
import com.khan.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

//--------------------Mapper Interface for Mapping and Conversion Between Entity and DTO Objects for Tag--------------------\\

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface TagMapper {
    @Mapping(target="postCount",source = "posts",qualifiedByName = "calculatePostCount")
    TagDto toTagResponse(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts){
        if(posts==null){return 0;}
        return (int) posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
