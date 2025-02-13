package org.fedesartu.percentage.service.business.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.fedesartu.percentage.service.business.exception.PercentageServiceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class PercentageClientTest {

    private static MockWebServer mockWebServer;

    private PercentageClient percentageClient;

    private WebClient webClient;

    @Mock
    private ReactiveStringRedisTemplate redis;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void setUpClient() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        webClient = WebClient.create(baseUrl);
        percentageClient = new PercentageClient(webClient, redis);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void findPercentageSuccess() throws JsonProcessingException {
        BigDecimal percentage = BigDecimal.TEN;
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(percentage))
                .addHeader("Content-Type", "application/json"));
        when(redis.opsForValue()).thenReturn(valueOperations);

        Mono<BigDecimal> responseMono = percentageClient.findPercentage();

        StepVerifier
                .create(responseMono)
                .consumeNextWith(result -> {
                    assertNotNull(result);
                    assertEquals(percentage, result);
                    verify(redis).opsForValue();
                    verify(valueOperations).set("percentage", result.toString());
                }).verifyComplete();
    }

    @Test
    void findPercentageFailButCached() {
        BigDecimal percentage = BigDecimal.TEN;
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        when(redis.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("percentage")).thenReturn(Mono.just(percentage.toString()));

        Mono<BigDecimal> responseMono = percentageClient.findPercentage();

        StepVerifier
                .create(responseMono)
                .consumeNextWith(result -> {
                    assertNotNull(result);
                    assertEquals(percentage, result);
                }).verifyComplete();
    }

    @Test
    void findPercentageError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500));
        when(redis.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("percentage")).thenReturn(Mono.empty());

        Mono<BigDecimal> responseMono = percentageClient.findPercentage();

        StepVerifier
                .create(responseMono)
                .expectError(PercentageServiceException.class)
                .verify();
    }

}
