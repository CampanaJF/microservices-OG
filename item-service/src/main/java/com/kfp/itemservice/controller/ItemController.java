package com.kfp.itemservice.controller;

import com.kfp.itemservice.dto.ProductDto;
import com.kfp.itemservice.model.Item;
import com.kfp.itemservice.service.ItemService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.math.BigDecimal.valueOf;

@AllArgsConstructor
@RestController
@RequestMapping("items")
public class ItemController {

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    private CircuitBreakerFactory circuitBreakerFactory;

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


        return circuitBreakerFactory.create("items")
                .run( () -> ResponseEntity.ok(itemService.findById(itemId, quantity)),
                        e -> getFallback(itemId, quantity, e));

    }

    private ResponseEntity<Item> getFallback(Long itemId, Integer quantity, Throwable e){

        logger.info(e.getMessage());

        ProductDto product = ProductDto.builder()
                .id(itemId)
                .price(valueOf(800.00))
                .name("Mug")
                .build();

        return ResponseEntity.ok(Item.builder().product(product).quantity(quantity).build());
    }


}
