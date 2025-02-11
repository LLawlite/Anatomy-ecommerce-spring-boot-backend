package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Long> {
    public Category findByCategoryName(String categoryName);
    public Optional<Category> findByCategoryId(Long categoryId);
}
