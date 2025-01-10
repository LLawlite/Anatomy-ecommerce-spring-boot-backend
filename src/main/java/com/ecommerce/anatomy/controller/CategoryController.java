package com.ecommerce.anatomy.controller;

import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.service.implementation.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    static long idno;

    @Autowired
    private CategoryServiceImpl categoryService;

    // Get all categories
    @GetMapping("/api/public/categories")
    public ResponseEntity< List<Category> >getCategories() {
        List<Category> categories=categoryService.getAllCategories();
        return  new ResponseEntity<>(categories,HttpStatus.OK);
    }

    // Add a new category
    @PostMapping("/api/admin/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        idno++;
        category.setCategoryId(idno);
        categoryService.addCategory(category);
        return new ResponseEntity<String>("Successfully created the category",HttpStatus.CREATED);
    }

    @DeleteMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") long categoryId) {
        try {
            String status= categoryService.deleteCategory(categoryId);
            return new ResponseEntity<String>(status, HttpStatus.OK);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<String>(e.getReason(),e.getStatusCode());
        }
    }

    @PutMapping("/api/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable("categoryId") long categoryId,
                                                 @RequestBody Category category) {
        try {
            String status= categoryService.updateCategory(categoryId, category);
            return new ResponseEntity<String>(status, HttpStatus.OK);
        }catch (ResponseStatusException e){
            return new ResponseEntity<String>(e.getReason(),e.getStatusCode());
        }
    }




}
