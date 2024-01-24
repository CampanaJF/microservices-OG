package com.kfp.productservice.service;

import com.kfp.productservice.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Long productId) throws InterruptedException;
}
