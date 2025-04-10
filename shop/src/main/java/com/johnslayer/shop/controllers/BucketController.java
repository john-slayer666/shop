package com.johnslayer.shop.controllers;

import com.johnslayer.shop.dto.BucketDTO;
import com.johnslayer.shop.service.BucketService;
import com.johnslayer.shop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;

@Controller
public class BucketController {
    private final BucketService bucketService;
    private final ProductService productService;


    public BucketController(BucketService bucketService,  ProductService productService) {
        this.bucketService = bucketService;
        this.productService = productService;
    }

    @GetMapping("/bucket")
    public String aboutBucket(Model model, Principal principal){
        if(principal == null){
            model.addAttribute("bucket", new BucketDTO());
        }else{
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

    @PostMapping("/removeFromBucket/{productId}")
    public String removeFromUserBucket(@PathVariable Long productId, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            productService.removeFromUserBucket(productId, username);
            return "redirect:/bucket";
        }
        return "redirect:/login";
    }

}






