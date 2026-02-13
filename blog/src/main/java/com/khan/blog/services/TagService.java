package com.khan.blog.services;

import com.khan.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

//--------------------Interface for Tag Services--------------------\\

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID id);
    Tag getTagById(UUID id);
    List<Tag> getTagByIds(Set<UUID>ids);
}
