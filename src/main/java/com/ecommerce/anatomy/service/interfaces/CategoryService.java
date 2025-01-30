package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.payload.CategoryDTO;
import com.ecommerce.anatomy.payload.CategoryDTOResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    CategoryDTOResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}

