package com.kfp.itemservice.service;

import com.kfp.itemservice.dto.ProductDto;
import com.kfp.itemservice.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long itemId, Integer quantity);
    void saveProduct(ProductDto productDto);
    void deleteProduct(Long productId);
    void updateProduct(Long productId, ProductDto productDto);

}
