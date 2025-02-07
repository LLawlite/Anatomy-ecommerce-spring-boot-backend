package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String productName,Pageable pageable);
    public Optional<Product> findByProductId(Long productId);
}
