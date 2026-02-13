package com.khan.blog.services;

import com.khan.blog.domain.CreatePostRequest;
import com.khan.blog.domain.UpdatePostRequest;
import com.khan.blog.domain.entities.Post;
import com.khan.blog.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//--------------------Interface for Post Services--------------------\\

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    void deletePost(UUID id);

}
