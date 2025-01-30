package com.ecommerce.anatomy.controller;
import com.ecommerce.anatomy.config.AppConsts;
import com.ecommerce.anatomy.payload.CategoryDTO;
import com.ecommerce.anatomy.payload.CategoryDTOResponse;
import com.ecommerce.anatomy.service.interfaces.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryDTOResponse> getAllCategories(
            @RequestParam(name="pageNumber",defaultValue = AppConsts.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name="pageSize",defaultValue = AppConsts.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConsts.SORT_Cateogries_BY,required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConsts.SORT_ORDER,required = false) String sortOrder

    ){
        CategoryDTOResponse categoryDTOResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryDTOResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

            CategoryDTO deleteCategory = categoryService.deleteCategory(categoryId);
            //return new ResponseEntity<>(status, HttpStatus.OK);
            //return ResponseEntity.ok(status);
            return new ResponseEntity<>(deleteCategory, HttpStatus.OK);

    }


    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId){

            CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }
}

