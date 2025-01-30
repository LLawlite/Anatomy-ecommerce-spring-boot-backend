package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category,Long> {
    public Category findByCategoryName(String categoryName);
}
