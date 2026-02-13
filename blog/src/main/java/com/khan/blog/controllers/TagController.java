package com.khan.blog.controllers;

import com.khan.blog.domain.dtos.CreateTagsRequest;
import com.khan.blog.domain.dtos.TagDto;
import com.khan.blog.domain.entities.Tag;
import com.khan.blog.mappers.TagMapper;
import com.khan.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//--------------------Controller for Functions and Controls Regarding Tags--------------------\\


@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    //--------------------Function and Mapping for Getting All Tags--------------------\\

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags(){
        List<Tag> tags = tagService.getTags();
        List<TagDto> tagRespons = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagRespons);
    }

    //--------------------Function and Mapping for Creating a Tag--------------------\\

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagsRequest createTagsRequest){
       List<Tag> savedTags = tagService.createTags(createTagsRequest.getNames());
       List<TagDto> createdTagRespons = savedTags.stream().map(tagMapper::toTagResponse).toList();
       return new ResponseEntity<>(
               createdTagRespons,
               HttpStatus.CREATED
       );
    }

    //--------------------Function and Mapping for Deleting a Tag Based on an ID--------------------\\

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
