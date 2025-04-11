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
import java.util.stream.Stream;

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
        bucket.setProducts(getCollectRefProductsByIds(productIds));
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
        List<Product> existingProducts = bucket.getProducts();

        //? Получаем текущие продукты как стрим (или пустой стрим, если список null)
        Stream <Product> currentProductsStream = existingProducts == null ?
                Stream.empty() : existingProducts.stream();

        //? Объединяем текущие продукты с новыми и сохраняем как новый список
        List<Product> updatedProducts = Stream.concat(
                currentProductsStream,
                getCollectRefProductsByIds(productIds).stream()
        ).collect(Collectors.toList());

        bucket.setProducts(updatedProducts);
        bucketRepository.save(bucket);
    }


    @Override
    public BucketDTO getBucketByUser(String name) {
        User user = userService.findByName(name);
        if(user == null || user.getBucket() == null) {
            return new BucketDTO();
        }

        BucketDTO bucketDTO = new BucketDTO();

        //? Stream и groupingBy для группировки продуктов по id
        List<BucketDetailsDTO> details = user.getBucket().getProducts().stream()
                .collect(Collectors.groupingBy(
                        Product::getId,
                        Collectors.reducing(
                                null,
                                BucketDetailsDTO::new,
                                (existing, newDetails) -> {
                                    if (existing == null) {
                                        return newDetails;
                                    }
                                    existing.setAmount(existing.getAmount().add(new BigDecimal(1.0)));
                                    existing.setSum(existing.getSum() + newDetails.getSum());
                                    return existing;
                                }
                        )
                ))
                .values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        bucketDTO.setBucketDetails(details);
        bucketDTO.aggregate();

        return bucketDTO;
    }

    @Override
    public void removeProducts(Bucket bucket, List<Long> longs) {
        List<Product> products = bucket.getProducts();
        if (products == null || products.isEmpty()) {
            return;
        }

        //? Map с подсчётом, сколько раз нужно удалить каждый id
        Map<Long, Long> toRemoveCount = longs.stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        //? Map для отслеживания уже удаленных продуктов по каждому id
        Map<Long, Integer> removedCounts = new HashMap<>();

        //? Фильтруем список
        List<Product> result = products.stream()
                .filter(product -> {
                    Long id = product.getId();
                    Long toRemove = toRemoveCount.get(id);
                    if (toRemove == null) {
                        return true; // Продукт не нужно удалять
                    }

                    int alreadyRemoved = removedCounts.getOrDefault(id, 0);
                    if (alreadyRemoved < toRemove) {
                        //? Удаляем
                        removedCounts.put(id, alreadyRemoved + 1);
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        bucket.setProducts(result);
        bucketRepository.save(bucket);
    }

}
