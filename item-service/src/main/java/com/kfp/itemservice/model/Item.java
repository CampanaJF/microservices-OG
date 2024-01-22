package com.kfp.itemservice.model;

import com.kfp.itemservice.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    private ProductDto product;
    private Integer quantity;

    public BigDecimal getTotal(){ // get prefix means it will be returner in a GET call
        return product.getPrice().multiply(valueOf(quantity));
    }
}
