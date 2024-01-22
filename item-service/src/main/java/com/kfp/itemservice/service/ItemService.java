package com.kfp.itemservice.service;

import com.kfp.itemservice.model.Item;

import java.util.List;

public interface ItemService {

    List<Item> findAll();
    Item findById(Long itemId, Integer quantity);
}
