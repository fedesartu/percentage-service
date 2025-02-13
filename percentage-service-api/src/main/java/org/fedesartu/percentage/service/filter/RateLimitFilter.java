package org.fedesartu.percentage.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.fedesartu.percentage.service.business.dto.RateLimitDto;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RateLimitFilter implements WebFilter {

    private static final Map<String, Integer> RATE_LIMIT_ENDPOINTS = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    private final ReactiveStringRedisTemplate redis;

    @PostConstruct
    public void postConstruct() {
        RATE_LIMIT_ENDPOINTS.put("/api/calculator", 3);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String endpoint = exchange.getRequest().getURI().getPath();
        if (!RATE_LIMIT_ENDPOINTS.containsKey(exchange.getRequest().getURI().getPath())) {
            return chain.filter(exchange);
        }

        Long maxRequests = RATE_LIMIT_ENDPOINTS.get(endpoint).longValue();

        String address = Objects.nonNull(exchange.getRequest().getRemoteAddress()) ? String.valueOf(exchange.getRequest().getRemoteAddress().getAddress()) : "127.0.0.1";
        long epoch = Instant.now().getEpochSecond();

        String requestKey = String.format("rl::%s::%s", endpoint, address);

        return redis.opsForValue().get(requestKey).defaultIfEmpty(StringUtils.EMPTY).flatMap(requestsCount -> {
            RateLimitDto count;
            try {
                count = mapper.readValue(requestsCount, RateLimitDto.class);
            } catch (JsonProcessingException e) {
                count = new RateLimitDto(0L, epoch);
            }

            RateLimitDto newRateLimit;
            if (count.epoch() < epoch - 60) {
                newRateLimit = new RateLimitDto(1L, epoch);
            } else {
                if (count.count() >= maxRequests) {
                    exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                    return exchange.getResponse().setComplete();
                } else {
                    newRateLimit = new RateLimitDto(count.count() + 1, count.epoch());
                }
            }

            try {
                return redis.opsForValue().set(requestKey, mapper.writeValueAsString(newRateLimit)).then(chain.filter(exchange));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
