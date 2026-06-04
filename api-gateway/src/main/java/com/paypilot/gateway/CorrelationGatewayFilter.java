package com.paypilot.gateway;

import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
class CorrelationGatewayFilter {
  static final String HEADER = "X-Correlation-Id";

  @Bean
  GlobalFilter correlationIdFilter() {
    return (exchange, chain) -> {
      String correlationId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HEADER))
          .filter(value -> !value.isBlank())
          .orElseGet(() -> UUID.randomUUID().toString());
      ServerHttpRequest request = exchange.getRequest().mutate()
          .header(HEADER, correlationId)
          .build();
      exchange.getResponse().getHeaders().set(HEADER, correlationId);
      return chain.filter(exchange.mutate().request(request).build());
    };
  }

  @Bean
  Ordered gatewayFilterOrderMarker() {
    return () -> -100;
  }
}
