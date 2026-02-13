package com.khan.blog.services.impl;

import com.khan.blog.domain.entities.Tag;
import com.khan.blog.repositories.TagRepository;
import com.khan.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//--------------------Implementation for Tag Services--------------------\\

@Service
@RequiredArgsConstructor

public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    //--------------------Function for Getting Tags--------------------\\

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    //--------------------Function for Creating Tags--------------------\\

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
      List<Tag> existingTags =  tagRepository.findByNameIn(tagNames);
      Set<String> existingTagNames = existingTags.stream()
              .map(Tag::getName)
              .collect(Collectors.toSet());

      List<Tag> newTags = tagNames.stream()
              .filter(name -> !existingTagNames.contains(name))
              .map(name -> Tag.builder()
                      .name(name)
                      .posts(new HashSet<>())
                      .build())
              .toList();

      List<Tag> savedTags = new ArrayList<>();
      if(!newTags.isEmpty()) savedTags = tagRepository.saveAll(newTags);

      savedTags.addAll(existingTags);
      return savedTags;
    }

    //--------------------Function for Deleting Tags--------------------\\

    @Transactional
    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag -> {
           if (!tag.getPosts().isEmpty()){
               throw new IllegalStateException("Cannot delete tag with posts");
           }
           tagRepository.deleteById(id);
        });

    }

    //--------------------Function for Getting a Specific Tag by ID--------------------\\

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Tag not found with ID " + id));
    }

    //--------------------Function for Getting a Specific Tag by Multiple IDs--------------------\\

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if(foundTags.size()!= ids.size()){
            throw new EntityNotFoundException("Not all specified tag IDs exist");
        }
        return foundTags;
    }
}
