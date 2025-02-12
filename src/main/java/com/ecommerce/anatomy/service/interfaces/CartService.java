package com.ecommerce.anatomy.service.interfaces;

import com.ecommerce.anatomy.payload.DTO.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
