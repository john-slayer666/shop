package com.johnslayer.shop.service;

import com.johnslayer.shop.dao.ProductRepository;
import com.johnslayer.shop.domain.User;
import com.johnslayer.shop.dto.ProductDTO;
import com.johnslayer.shop.dto.UserDTO;
import com.johnslayer.shop.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper = ProductMapper.MAPPER;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(productRepository.findAll());
    }


}

