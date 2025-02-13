package org.fedesartu.percentage.service.business.client;

import lombok.RequiredArgsConstructor;
import org.fedesartu.percentage.service.business.exception.PercentageServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.fedesartu.percentage.service.business.exception.ExceptionCode.CANNOT_GET_PERCENTAGE;


@Service
@RequiredArgsConstructor
public class PercentageClient {

    @Qualifier("percentage-api-client")
    private final WebClient webClient;

    private final ReactiveStringRedisTemplate redis;

    public Mono<BigDecimal> findPercentage() {
        return webClient
                .get()
                .uri("percentage")
                .retrieve()
                .bodyToMono(BigDecimal.class)
                .retry(3)
                .onErrorResume(error -> redis.opsForValue().get("percentage")
                        .switchIfEmpty(Mono.error(new PercentageServiceException(CANNOT_GET_PERCENTAGE, "Cannot find percentage")))
                        .flatMap(result -> Mono.just(BigDecimal.valueOf(Long.parseLong(result))))
                )
                .doOnSuccess(value -> redis.opsForValue().set("percentage", value.toString()));
    }

}
