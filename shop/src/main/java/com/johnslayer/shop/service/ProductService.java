package com.johnslayer.shop.service;

import com.johnslayer.shop.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void addToUserBucket(Long productId, String username);


    @Transactional
    void removeFromUserBucket (Long productId, String username);
}
