package com.gateway.service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class RoadmapGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(RoadmapGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //logger.info("pre filtering");

        exchange.getRequest()
                .mutate()
                .headers( header -> header.add("token","8761"));

        return chain.filter(exchange)
                .then(Mono.fromRunnable( () -> {
                    //logger.info("post filtering");

                    exchange.getResponse()
                            .getCookies()
                            .add("color", ResponseCookie.from("color","orange").build());

                    Optional.ofNullable(exchange.getRequest()
                            .getHeaders()
                            .getFirst("token")).ifPresent( value -> {
                                exchange.getResponse().getHeaders().add("token", value);
                    });

                }));
    }


    @Override
    public int getOrder() {
        return 100;
    }

    // exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
}
