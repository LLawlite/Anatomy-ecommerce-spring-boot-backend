package com.ecommerce.anatomy.service.implementation;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.repositories.CategoryRepository;
import com.ecommerce.anatomy.service.interfaces.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    // Get All the categories
    @Override
    public List<Category> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                logger.info("No categories found in the database.");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No categories available.");
            }
            return categories;
        } catch (Exception ex) {
            logger.error("Error retrieving categories: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve categories.");
        }
    }

    // Create a new category
    @Override
    public void createCategory(Category category) {
        try {
            if (category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
                logger.warn("Attempt to create category with empty name.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name must not be null or empty.");
            }
            categoryRepository.save(category);
            logger.info("Category created successfully with ID: {}", category.getCategoryId());
        } catch (Exception ex) {
            logger.error("Error creating category: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create category.");
        }
    }

    // Delete a category
    @Override
    public String deleteCategory(Long categoryId) {
        try {
            if (!categoryRepository.existsById(categoryId)) {
                logger.warn("Category with ID: {} not found for deletion.", categoryId);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
            }
            categoryRepository.deleteById(categoryId);
            logger.info("Category with ID: {} deleted successfully.", categoryId);
            return "Category with categoryId: " + categoryId + " deleted successfully !!";
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error deleting category: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete category.");
        }
    }

    // Update a category
    @Override
    public Category updateCategory(Category updatedCategory, Long categoryId) {
        try {
            if (updatedCategory == null || updatedCategory.getCategoryName() == null || updatedCategory.getCategoryName().isEmpty()) {
                logger.warn("Invalid category details provided for update.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category details must not be null or empty.");
            }

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> {
                        logger.warn("Category with ID: {} not found for update.", categoryId);
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
                    });

            category.setCategoryName(updatedCategory.getCategoryName());
            Category savedCategory = categoryRepository.save(category);
            logger.info("Category with ID: {} updated successfully.", categoryId);
            return savedCategory;
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error updating category: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update category.");
        }
    }
}
