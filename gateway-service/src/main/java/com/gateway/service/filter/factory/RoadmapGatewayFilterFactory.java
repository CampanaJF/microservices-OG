package com.gateway.service.filter.factory;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class RoadmapGatewayFilterFactory extends AbstractGatewayFilterFactory<RoadmapGatewayFilterFactory.Configuration> {

    private final Logger logger = LoggerFactory.getLogger(RoadmapGatewayFilterFactory.class);

    public RoadmapGatewayFilterFactory() {
        super(Configuration.class);
    }

    @Override
    public GatewayFilter apply(Configuration config) {
        return (exchange, chain) -> {

            logger.info("pre gateway filter factory " + config.message);

            return chain.filter(exchange).then(Mono.fromRunnable( () -> {

                Optional.ofNullable(config.cookieValue).ifPresent(cookie -> {
                    exchange.getResponse()
                            .addCookie(ResponseCookie.from(config.cookieName, config.cookieValue).build());
                });

                logger.info("post gateway filter factory " + config.message);


            }));
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("message", "cookieName", "cookieValue");
    }

    @Override
    public String name() {
        return "SesameStreet";
    }

    @Getter
    @Setter
    public static class Configuration {

        private String message;

        private String cookieValue;

        private String cookieName;

    }

}
