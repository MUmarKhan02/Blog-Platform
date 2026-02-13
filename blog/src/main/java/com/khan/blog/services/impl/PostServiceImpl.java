package com.khan.blog.services.impl;

import com.khan.blog.domain.CreatePostRequest;
import com.khan.blog.domain.PostStatus;
import com.khan.blog.domain.UpdatePostRequest;
import com.khan.blog.domain.entities.Category;
import com.khan.blog.domain.entities.Post;
import com.khan.blog.domain.entities.Tag;
import com.khan.blog.domain.entities.User;
import com.khan.blog.repositories.PostRepository;
import com.khan.blog.services.CategoryService;
import com.khan.blog.services.PostService;
import com.khan.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//--------------------Implementation for Post Services--------------------\\

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE=200;

    //--------------------Function for Getting a Post--------------------\\

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not exist with ID "+id));
    }

    //--------------------Function for Getting All Posts--------------------\\

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId !=null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,category,tag
            );

        }
        if(categoryId!=null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }
        if(tagId!=null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,tag
            );
        }
        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    //--------------------Function for Getting The Posts under Draft Status--------------------\\

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);

    }

    //--------------------Function for Creating a Post--------------------\\

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

       Category category= categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);
       Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    //--------------------Function for Updating a Post--------------------\\

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
       Post existingPost = postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Post does not exist with id "+id));

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

       Set<UUID> existingTagIds= existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
       Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
       if(!existingTagIds.equals(updatePostRequestTagIds)){
           List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
           existingPost.setTags(new HashSet<>(newTags));
       }
       return postRepository.save(existingPost);
    }

    //--------------------Function for Deleting a Post--------------------\\

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    //--------------------Function for Calculating and Generating Reading Time--------------------\\

    private Integer calculateReadingTime(String content){
        if(content==null || content.isEmpty()) return 0;
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
