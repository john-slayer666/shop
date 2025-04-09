package com.johnslayer.shop.service;

import com.johnslayer.shop.domain.Bucket;
import com.johnslayer.shop.domain.User;
import com.johnslayer.shop.dto.BucketDTO;

import java.util.List;
import java.util.Optional;

public interface BucketService {
    Bucket createBucket(User user , List<Long> productIds);
    void addProducts(Bucket bucket, List<Long> productIds);

    BucketDTO getBucketByUser(String name);

    void removeFromBucket(User user, Long productId);

    Optional<User> getUserById(Long id);
}
