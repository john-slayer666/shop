package com.johnslayer.shop.controllers;

import com.johnslayer.shop.dto.ProductDTO;
import com.johnslayer.shop.service.BucketService;
import com.johnslayer.shop.service.ProductService;
import com.johnslayer.shop.service.SessionObjectHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final SessionObjectHolder sessionObjectHolder;

    private final BucketService bucketService;
    public ProductController(ProductService productService, SessionObjectHolder sessionObjectHolder, BucketService bucketService) {
        this.productService = productService;
        this.sessionObjectHolder = sessionObjectHolder;
        this.bucketService = bucketService;
    }

    @GetMapping
    public String list(Model model){
        sessionObjectHolder.addClick();

        List<ProductDTO> list = productService.getAll();
        model.addAttribute("products", list);
        return "products";
    }

    @GetMapping("/{id}/bucket")
    public String addBucket(@PathVariable Long id, Principal principal){
        sessionObjectHolder.addClick();

        if(principal == null){
            return "redirect:/products";
        }
        productService.addToUserBucket(id, principal.getName());
        return "redirect:/products";
    }

}
