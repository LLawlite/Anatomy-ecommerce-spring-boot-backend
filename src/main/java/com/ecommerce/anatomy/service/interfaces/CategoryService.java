package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.payload.DTO.CategoryDTO;
import com.ecommerce.anatomy.payload.Response.CategoryDTOResponse;

public interface CategoryService {
    CategoryDTOResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}

