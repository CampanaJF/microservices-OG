package com.kfp.productservice.controller;

import com.kfp.productservice.exception.ProductNotFoundException;
import com.kfp.productservice.model.Product;
import com.kfp.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${server.port}")
    private Integer port;

    @GetMapping
    public ResponseEntity<List<Product>> list(){

        List<Product> list = productService.findAll()
                .stream()
                .peek(product -> product.setPort(port))
                .toList();


        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> get(@PathVariable("productId") Long productId){
        try{

            Product product = productService.findById(productId);
            product.setPort(port);

            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch(ProductNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
