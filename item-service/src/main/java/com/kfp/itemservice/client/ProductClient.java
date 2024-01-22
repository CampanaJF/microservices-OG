package com.kfp.itemservice.client;

import com.kfp.itemservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-msvc")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductDto> list();

    @GetMapping("products/{productId}")
    ProductDto get(@PathVariable("productId") Long productId);
}
