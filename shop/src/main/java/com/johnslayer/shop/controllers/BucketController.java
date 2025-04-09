package com.johnslayer.shop.controllers;

import com.johnslayer.shop.domain.User;
import com.johnslayer.shop.dto.BucketDTO;
import com.johnslayer.shop.service.BucketService;
import com.johnslayer.shop.service.UserService;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.security.Principal;
import java.util.Optional;

@Controller
public class BucketController {
    private final BucketService bucketService;
    private  final UserService userService;

    public BucketController(BucketService bucketService, UserService userService) {
        this.bucketService = bucketService;
        this.userService = userService;
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
    public String removeProductFromBucket(@PathVariable Long productId, Principal principal) {
        // Получаем текущего пользователя
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByName(username);

            if (user != null) {
                //? Удаляем товар из корзины пользователя по productId
                bucketService.removeFromBucket(user, productId);
                return "redirect:/bucket"; // Перенаправление на страницу корзины
            }
        }
        return "redirect:/login";
    }

}






