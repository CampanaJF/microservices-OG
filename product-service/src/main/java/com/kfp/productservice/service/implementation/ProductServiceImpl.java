package com.kfp.productservice.service.implementation;

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
        return productJpaRepository.findById(productId).orElse(null);
    }

    @Override
    public void save(Product product) {
        productJpaRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public void update(Long productId, Product product) {

        Product toUpdate = findById(productId);

        toUpdate.setName(product.getName());
        toUpdate.setPrice(product.getPrice());
        toUpdate.setCreationDate(product.getCreationDate());

        productJpaRepository.save(toUpdate);
    }
}
