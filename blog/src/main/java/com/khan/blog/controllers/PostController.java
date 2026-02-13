package com.khan.blog.controllers;


import com.khan.blog.domain.CreatePostRequest;
import com.khan.blog.domain.UpdatePostRequest;
import com.khan.blog.domain.dtos.CreatePostRequestDto;
import com.khan.blog.domain.dtos.PostDto;
import com.khan.blog.domain.dtos.UpdatePostRequestDto;
import com.khan.blog.domain.entities.Post;
import com.khan.blog.domain.entities.User;
import com.khan.blog.mappers.PostMapper;
import com.khan.blog.services.PostService;
import com.khan.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//--------------------Controller for Functions and Controls Regarding Posts--------------------\\


@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    //--------------------Function and Mapping for Getting All Posts--------------------\\


    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false) UUID tagId){
        List<Post> posts =  postService.getAllPosts(categoryId,tagId);
        List<PostDto> postDtos= posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);

    }

    //--------------------Function and Mapping for Getting the Drafts and Navigate Drafts--------------------\\


    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){
       User loggedInUser =  userService.getUserById(userId);
       List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
       List<PostDto> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
       return ResponseEntity.ok(postDtos);
    }

    //--------------------Function and Mapping for Creating a Post--------------------\\


    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            ){
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost= postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);

        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    //--------------------Function and Mapping for Updating a Post Based on an ID--------------------\\


    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto){
            UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
            Post updatedPost =postService.updatePost(id,updatePostRequest);
            PostDto updatedPostDto = postMapper.toDto(updatedPost);
            return ResponseEntity.ok(updatedPostDto);
    }

    //--------------------Function and Mapping for Getting a Post Based on an ID-------------------\\

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id){
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    //--------------------Function and Mapping for Deleting a Post Based on an ID--------------------\\

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
