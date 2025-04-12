package com.ecommerce.anatomy.repositories;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String productName,Pageable pageable);
    public Optional<Product> findByProductId(Long productId);
    // Fetch Top 3 Best Selling Products
    @Query("SELECT p FROM Product p ORDER BY p.saleCount DESC LIMIT 3")
    List<Product> findTop3BestSellers();
}
