package com.kfp.productservice.service;

import com.commons.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long productId) throws InterruptedException;
    void save(Product product);
    void delete(Long productId);
    void update(Long productId, Product product);
}
