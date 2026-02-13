package com.khan.blog.services;
import com.khan.blog.domain.entities.Category;
import java.util.List;
import java.util.UUID;

//--------------------Interface for Category Services--------------------\\

public interface CategoryService {
 List<Category> listCategories();
 Category createCategory(Category category);
 void deleteCategory(UUID id);
 Category getCategoryById(UUID id);
}
