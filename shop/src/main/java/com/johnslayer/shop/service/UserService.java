package com.johnslayer.shop.service;

import com.johnslayer.shop.domain.User;
import com.johnslayer.shop.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean save(UserDTO userDTO);
    List<UserDTO> getAll();

    User findByName(String name);

    void updateProfile(UserDTO userDTO);
}
