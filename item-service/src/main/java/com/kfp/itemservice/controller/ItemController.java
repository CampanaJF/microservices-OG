package com.kfp.itemservice.controller;

import com.kfp.itemservice.model.Item;
import com.kfp.itemservice.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("items")
public class ItemController {

    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "token-request", required = false) String token){

        System.out.println(name + token);
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{itemId}/quantity/{quantity}")
    public ResponseEntity<Item> get(
            @PathVariable("itemId") Long itemId,
            @PathVariable("quantity") Integer quantity){

        return new ResponseEntity<>(itemService.findById(itemId, quantity), HttpStatus.OK);

    }

}
