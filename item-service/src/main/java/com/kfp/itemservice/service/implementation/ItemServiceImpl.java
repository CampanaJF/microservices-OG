package com.kfp.itemservice.service.implementation;

import com.kfp.itemservice.client.ProductClient;
import com.kfp.itemservice.dto.ProductDto;
import com.kfp.itemservice.model.Item;
import com.kfp.itemservice.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ProductClient restProductClient;
    @Override
    public List<Item> findAll() {

        return restProductClient.list().stream()
                .map(product -> new Item(product,1))
                .toList();
    }

    @Override
    public Item findById(Long itemId, Integer quantity) {
        return new Item(restProductClient.get(itemId),quantity);
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        restProductClient.save(productDto);
    }

    @Override
    public void deleteProduct(Long productId) {
        restProductClient.delete(productId);
    }

    @Override
    public void updateProduct(Long productId, ProductDto productDto) {
        restProductClient.update(productId, productDto);
    }
}
