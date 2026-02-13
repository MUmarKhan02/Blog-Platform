package com.khan.blog.repositories;
import java.util.List;

import com.khan.blog.domain.entities.Category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//--------------------Repository Information Regarding Category--------------------\\

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    boolean existsByNameIgnoreCase(String name);
}
