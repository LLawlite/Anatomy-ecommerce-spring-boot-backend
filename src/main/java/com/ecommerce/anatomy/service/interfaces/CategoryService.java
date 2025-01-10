package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    public List<Category>getAllCategories();
    public String addCategory(Category category);
    public String deleteCategory(long categoryId);
    public String updateCategory(long categoryId, Category category);

}
