package com.ecommerce.anatomy.service.implementation;

import com.ecommerce.anatomy.exceptions.APIException;
import com.ecommerce.anatomy.exceptions.ResourceNotFoundException;
import com.ecommerce.anatomy.model.Category;
import com.ecommerce.anatomy.model.Product;
import com.ecommerce.anatomy.payload.DTO.ProductDTO;
import com.ecommerce.anatomy.payload.Response.ProductResponse;
import com.ecommerce.anatomy.repositories.CategoryRepository;
import com.ecommerce.anatomy.repositories.ProductRepository;
import com.ecommerce.anatomy.service.interfaces.FileService;
import com.ecommerce.anatomy.service.interfaces.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No productgs found");
        }

        List<ProductDTO>productDTOS=products.stream()
                .map(product->modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalpages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            String sortBy,
                                            String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Category category= categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
        List<Product> products = productPage.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No productgs found  ");
        }

        List<ProductDTO>productDTOS=products.stream()
                .map(product->modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalpages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String productName,Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> productPage=productRepository.findByProductNameContainingIgnoreCase(productName,pageDetails);
        List<Product> products = productPage.getContent();

        if(products.isEmpty())
        {
            throw new APIException("No productgs found with keyword  " +productName);
        }
        List<ProductDTO>productDTOS=products.stream()
                .map(product->modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalpages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());


        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO,Long productId) {
        if(productDTO==null || productDTO.getProductName()==null || productDTO.getProductName().isEmpty())
        {
            throw new APIException("Product fields cannot be null");
        }

        // Get Existing Product
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));

        // Update fields
        product.setProductName(productDTO.getProductName());
        product.setQuantity(productDTO.getQuantity());
        product.setPrice(productDTO.getPrice());
        product.setDiscount(productDTO.getDiscount());

        // Calculate special price
        double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() * 0.01) * productDTO.getPrice());
        product.setSpecialPrice(specialPrice);

        // Save the updated product
        Product updatedProduct = productRepository.save(product);

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct,ProductDTO.class);

        // Convert back to DTO and return
        return updatedProductDTO;




    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product=productRepository.findByProductId(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));

        productRepository.delete(product);
        ProductDTO deletedProductDTO=modelMapper.map(product,ProductDTO.class);
        return deletedProductDTO;
    }




    // Add a product
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId)
    {

        Category category= categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));

        boolean isProductPresent=false;
        List<Product>products=category.getProducts();
        for(Product value:products)
        {
            if(value.getProductName().equalsIgnoreCase(productDTO.getProductName()))
            {
                isProductPresent=true;
                break;
            }
        }
        if(!isProductPresent) {


            Product product = modelMapper.map(productDTO, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * .01) * product.getPrice());
            product.setSpecialPrice(specialPrice);

            Product savedProduct = productRepository.save(product);
            ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

            return savedProductDTO;
        }
        else{
            throw new APIException("Product with same name already exists in this category");
        }
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Upload image to server
        // Get the file name of uploaded image

        String fileName = fileService.uploadImage(path, image);

        // Updating the new file name to the product
        productFromDb.setImage(fileName);

        // Save updated product
        Product updatedProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}
