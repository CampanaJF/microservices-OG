package com.kfp.productservice.controller;

import com.kfp.productservice.model.Product;
import com.kfp.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> list(){

        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> get(@PathVariable("productId") Long productId) throws InterruptedException {
        if(productId == 19L){
            throw new IllegalStateException("Product not found!");
        }
        if(productId == 4L){
            TimeUnit.SECONDS.sleep(5);
        }
        return new ResponseEntity<>(productService.findById(productId), HttpStatus.OK);
    }
}
