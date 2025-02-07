package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category,Long> {
    public Category findByCategoryName(String categoryName);
    public Optional<Category> findByCategoryId(Long categoryId);
}
