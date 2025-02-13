package org.fedesartu.percentage.service.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fedesartu.percentage.service.business.dto.RateLimitDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class RateLimitFilterTest {

    @Mock
    private ReactiveStringRedisTemplate redis;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    @InjectMocks
    private RateLimitFilter rateLimitFilter;

    @Test
    void failRateLimit() throws JsonProcessingException {
        rateLimitFilter.postConstruct();

        String key = "rl::/api/calculator::127.0.0.1";
        long epoch = Instant.now().getEpochSecond();
        RateLimitDto firstLimit = new RateLimitDto(0L, epoch);
        RateLimitDto secondLimit = new RateLimitDto(1L, epoch);
        RateLimitDto thirdLimit = new RateLimitDto(2L, epoch);
        RateLimitDto fourthLimit = new RateLimitDto(3L, epoch);

        when(redis.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(firstLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(secondLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(thirdLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(fourthLimit)));
        when(valueOperations.set(any(), any())).thenReturn(Mono.just(true));

        WebTestClient client = WebTestClient.bindToWebHandler(exchange -> Mono.empty())
                .webFilter(rateLimitFilter)
                .build();

        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void resetRateLimit() throws JsonProcessingException {
        rateLimitFilter.postConstruct();

        String key = "rl::/api/calculator::127.0.0.1";
        long epoch = Instant.now().getEpochSecond();
        RateLimitDto firstLimit = new RateLimitDto(0L, epoch);
        RateLimitDto secondLimit = new RateLimitDto(1L, epoch - 70);
        RateLimitDto thirdLimit = new RateLimitDto(2L, epoch - 70);
        RateLimitDto fourthLimit = new RateLimitDto(3L, epoch - 70);

        when(redis.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(firstLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(secondLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(thirdLimit)))
                .thenReturn(Mono.just(new ObjectMapper().writeValueAsString(fourthLimit)));
        when(valueOperations.set(any(), any())).thenReturn(Mono.just(true));

        WebTestClient client = WebTestClient.bindToWebHandler(exchange -> Mono.empty())
                .webFilter(rateLimitFilter)
                .build();

        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
        client.post().uri("/api/calculator")
                .exchange()
                .expectStatus().isOk();
    }

}
