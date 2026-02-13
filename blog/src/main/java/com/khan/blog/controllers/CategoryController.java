package com.khan.blog.controllers;

import java.util.List;
import java.util.UUID;

import com.khan.blog.domain.dtos.CreateCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.khan.blog.domain.entities.Category;
import com.khan.blog.domain.dtos.CategoryDto;
import com.khan.blog.mappers.CategoryMapper;
import com.khan.blog.services.CategoryService;

//--------------------Controller for Functions and Controls Regarding Categories--------------------\\

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    //--------------------Function and Mapping for Listing The Categories--------------------\\

    @GetMapping
    public ResponseEntity<List<CategoryDto>>listCategories(){
        List<CategoryDto>categories = categoryService.listCategories().stream().map(categoryMapper::toDto).toList();
        return ResponseEntity.ok(categories);
    }

    //--------------------Function and Mapping for Creating a Category--------------------\\

    @PostMapping
    public ResponseEntity<CategoryDto>createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest){
        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
        Category savedCategory = categoryService.createCategory(categoryToCreate);

        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }

    //--------------------Function and Mapping for Deleting a Category--------------------\\

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id){
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
