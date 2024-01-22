package com.kfp.productservice.service.implementation;

import com.kfp.productservice.exception.ProductNotFoundException;
import com.kfp.productservice.model.Product;
import com.kfp.productservice.repository.ProductJpaRepository;
import com.kfp.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductJpaRepository productJpaRepository;
    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Product findById(Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow( () -> new ProductNotFoundException(
                        String.format("No product with id: %s",productId)));
    }
}
