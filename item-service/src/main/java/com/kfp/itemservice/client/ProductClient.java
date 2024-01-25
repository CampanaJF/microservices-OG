package com.kfp.itemservice.client;

import com.kfp.itemservice.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-msvc")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductDto> list();

    @GetMapping("products/{productId}")
    ProductDto get(@PathVariable("productId") Long productId);

    @PostMapping("/products")
    void save(@RequestBody ProductDto productDto);

    @DeleteMapping("products/{productId}")
    void delete(@PathVariable("productId") Long productId);

    @PutMapping("products/{productId}")
    void update(@PathVariable("productId") Long productId, @RequestBody ProductDto productDto);
}
