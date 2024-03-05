package com.kfp.itemservice.controller;

import com.kfp.itemservice.dto.ProductDto;
import com.kfp.itemservice.model.Item;
import com.kfp.itemservice.service.ItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.math.BigDecimal.valueOf;


@RestController
@RefreshScope
@RequestMapping("items")
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    @Value("${configuration.text}")
    private String text;

    @Autowired
    private Environment env;

    @Autowired
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

    @PostMapping("/products")
    public ResponseEntity<Void> save(@RequestBody ProductDto productDto){
        itemService.saveProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> delete(@PathVariable("productId") Long productId){
        itemService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Void> update(
            @PathVariable("productId") Long productId,
            @RequestBody ProductDto productDto){

        itemService.updateProduct(productId, productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Using annotations only allows for application configs, no file configs
    @CircuitBreaker(name = "items", fallbackMethod = "getFallback")
    @GetMapping("alt/{itemId}/quantity/{quantity}")
    public ResponseEntity<Item> getAlt(
            @PathVariable("itemId") Long itemId,
            @PathVariable("quantity") Integer quantity){

        return ResponseEntity.ok(itemService.findById(itemId, quantity));
    }

    // Does not work like circuit breaker, only time out, can be combined with circuit breaker
    @CircuitBreaker(name = "items", fallbackMethod = "getFuture")
    @TimeLimiter(name = "items")
    @GetMapping("timed/{itemId}/quantity/{quantity}")
    public CompletableFuture<ResponseEntity<Item>> getAltTime(
            @PathVariable("itemId") Long itemId,
            @PathVariable("quantity") Integer quantity){

        return CompletableFuture.supplyAsync(
                        () -> ResponseEntity.ok((itemService.findById(itemId, quantity))));
    }

    private ResponseEntity<Item> getFallback(Long itemId, Integer quantity, Throwable e){

        ProductDto product = ProductDto.builder()
                .id(itemId)
                .price(valueOf(800.00))
                .name("Mug")
                .build();

        return ResponseEntity.ok(Item.builder().product(product).quantity(quantity).build());
    }

    private CompletableFuture<ResponseEntity<Item>> getFuture(Long itemId, Integer quantity, Throwable e){

        ProductDto product = ProductDto.builder()
                .id(itemId)
                .price(valueOf(800.00))
                .name("Mug")
                .build();

        return CompletableFuture.supplyAsync(
                        () -> ResponseEntity.ok(
                                (Item.builder().product(product).quantity(quantity).build())));
    }

    @GetMapping("/get-config")
    public ResponseEntity<?> config(@Value("${server.port}")String port){

        log.info(text);
        log.info(port);

        Map<String, String> json = new HashMap<>();
        json.put("text", text);
        json.put("port",port);

        if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")){
            json.put("email", env.getProperty("configuration.autor.email"));
            json.put("name", env.getProperty("configuration.autor.name"));
        }

        return new ResponseEntity<Map<String, String>>(json,HttpStatus.OK);
    }

}
