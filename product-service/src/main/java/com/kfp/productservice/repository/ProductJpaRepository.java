package com.kfp.productservice.repository;

import com.kfp.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}
