package com.ecommerce.anatomy.service.implementation;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.service.interfaces.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories=new ArrayList<>();
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public String addCategory(Category category) {
        categories.add(category);
        return " Category added succefully" ;
    }
    @Override
    public String deleteCategory(long categoryId) {
        Category category=categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        categories.remove(category);
        return "Delted Successfully";
    }

    @Override
    public String updateCategory(long categoryId, Category category) {
        Category existingCategory=categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        existingCategory.setCategoryName(category.getCategoryName());
        return "Updated Successfully";
    }
}
