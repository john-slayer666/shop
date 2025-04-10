package com.johnslayer.shop.service;

import com.johnslayer.shop.dao.BucketRepository;
import com.johnslayer.shop.dao.ProductRepository;
import com.johnslayer.shop.domain.Bucket;
import com.johnslayer.shop.domain.Product;
import com.johnslayer.shop.domain.User;
import com.johnslayer.shop.dto.BucketDTO;
import com.johnslayer.shop.dto.BucketDetailsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }


    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream()
                //*getOne вытаскивает ссылку на объект, findOne сам объект
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }


    @Override
    public void addProducts(Bucket bucket, List<Long> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }


    @Override
    public BucketDTO getBucketByUser(String name) {
        User user = userService.findByName(name);
        if(user == null || user.getBucket() == null) {
            return new BucketDTO();
        }

        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailsDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for(Product product : products) {
            BucketDetailsDTO details = mapByProductId.get(product.getId());
            if(details == null) {
                mapByProductId.put(product.getId(), new BucketDetailsDTO(product));
            } else {
                details.setAmount(details.getAmount().add(new BigDecimal(1.0)));
                details.setSum(details.getSum() + Double.valueOf(product.getPrice().toString()));
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }

    @Override
    public void removeProducts(Bucket bucket, List<Long> longs) {
        List<Product> products = bucket.getProducts();
        if (products == null || products.isEmpty()) {
            return;
        }

        List<Product> updatedProductList = new ArrayList<>(products);

        for (Long idToRemove : longs) {
            for (Iterator<Product> iterator = updatedProductList.iterator(); iterator.hasNext(); ) {
                Product product = iterator.next();
                if (product.getId().equals(idToRemove)) {
                    iterator.remove(); //? удаляем только первый найденный
                    break;
                }
            }
        }

        bucket.setProducts(updatedProductList);
        bucketRepository.save(bucket);
    }

}
