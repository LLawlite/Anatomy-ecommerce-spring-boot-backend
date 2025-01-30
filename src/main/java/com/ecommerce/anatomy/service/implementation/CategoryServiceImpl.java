package com.ecommerce.anatomy.service.implementation;

import com.ecommerce.anatomy.exceptions.APIException;
import com.ecommerce.anatomy.exceptions.ResourceNotFoundException;
import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.payload.CategoryDTO;
import com.ecommerce.anatomy.payload.CategoryDTOResponse;
import com.ecommerce.anatomy.repositories.CategoryRepository;
import com.ecommerce.anatomy.service.interfaces.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ModelMapper modelMapper;

    // Get All the categories
    @Override
    public CategoryDTOResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

            Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
            Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

            List<Category> categories = categoryPage.getContent();
            if(categories.isEmpty())
            {
                throw new APIException("No categories created yet.");
            }
            List<CategoryDTO>categoryDTOS=categories.stream()
                    .map(category->modelMapper.map(category,CategoryDTO.class))
                    .toList();
            CategoryDTOResponse categoryDTOResponse=new CategoryDTOResponse();
            categoryDTOResponse.setContent(categoryDTOS);
            categoryDTOResponse.setPageNumber(categoryPage.getNumber());
            categoryDTOResponse.setPageSize(categoryPage.getSize());
            categoryDTOResponse.setTotalElements(categoryPage.getTotalElements());
            categoryDTOResponse.setTotalpages(categoryPage.getTotalPages());
            categoryDTOResponse.setLastPage(categoryPage.isLast());

            return categoryDTOResponse;
    }

    // Create a new category
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

            if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().isEmpty()) {
                throw new APIException( "Category name must not be null or empty.");
            }
            Category savedCategoryDb=categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
            if(savedCategoryDb!=null){
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with the same name already exists.");
                throw new APIException( "Category with the same name already exists.");
            }
            Category category = modelMapper.map(categoryDTO, Category.class);
            Category savedCategory=categoryRepository.save(category);
            CategoryDTO savedCategoryDTO=modelMapper.map(savedCategory, CategoryDTO.class);

            return savedCategoryDTO ;


    }

    // Delete a category
    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
            Category category=categoryRepository.findById(categoryId)
                            .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
            categoryRepository.deleteById(categoryId);
            CategoryDTO deleteCategory=modelMapper.map(category,CategoryDTO.class);
            return deleteCategory;


    }

    // Update a category
    @Override
    public CategoryDTO updateCategory(CategoryDTO updatedCategoryDTO, Long categoryId) {

            if (updatedCategoryDTO == null || updatedCategoryDTO.getCategoryName() == null || updatedCategoryDTO.getCategoryName().isEmpty()) {
                throw new APIException( "Category details must not be null or empty.");
            }


            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> {
                        return new ResourceNotFoundException("Category","categoryId",categoryId);
                    });

            category.setCategoryName(updatedCategoryDTO.getCategoryName());
            Category savedCategory = categoryRepository.save(category);

            CategoryDTO savedCategoryDTO=modelMapper.map(savedCategory,CategoryDTO.class);
            return savedCategoryDTO;

    }
}
