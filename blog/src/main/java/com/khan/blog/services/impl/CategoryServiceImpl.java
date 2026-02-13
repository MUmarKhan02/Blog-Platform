package com.khan.blog.services.impl;
import com.khan.blog.services.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.khan.blog.domain.entities.Category;
import com.khan.blog.repositories.CategoryRepository;

//--------------------Implementation for Category Services--------------------\\

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    //--------------------Function for Listing Categories--------------------\\

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    //--------------------Function for Creating Categories--------------------\\

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();
       if( categoryRepository.existsByNameIgnoreCase(categoryName)){
           throw new IllegalArgumentException("Category already exists with name: "+categoryName);
       }
       return categoryRepository.save(category);
    }

    //--------------------Function for Deleting Categories--------------------\\

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){
            if(!category.get().getPosts().isEmpty()){
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.deleteById(id);
        }
    }

    //--------------------Function for Getting Categories By ID--------------------\\

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id"+id));
    }


}
