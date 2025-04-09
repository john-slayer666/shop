package com.johnslayer.shop.service;

import com.johnslayer.shop.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void addToUserBucket(Long productId, String username);


}
