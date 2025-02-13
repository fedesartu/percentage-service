package org.fedesartu.percentage.service.business.services;

import org.fedesartu.percentage.service.business.dto.RequestLogDto;
import org.fedesartu.percentage.service.model.entity.postgres.RequestLog;
import org.fedesartu.percentage.service.model.repository.postgres.RequestLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class RequestLogServiceTest {

    @InjectMocks
    private RequestLogService requestLogService;

    @Mock
    private RequestLogRepository requestLogRepository;

    @Test
    void find() {
        String url = "testUrl";
        Integer page = 1;
        Integer size = 10;
        java.time.LocalDateTime dateFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime dateTo = LocalDateTime.now();
        RequestLog log = new RequestLog();
        log.setId(1);

        when(requestLogRepository.findAllFiltered(url, dateFrom, dateTo, page, size))
                .thenReturn(Flux.just(log));

        Flux<RequestLogDto> resultFlux = requestLogService.find(page, size, url, dateFrom, dateTo);

        StepVerifier
                .create(resultFlux)
                .consumeNextWith(result -> {
                    assertNotNull(result);
                    assertEquals(log.getId(), result.id());
                }).verifyComplete();
    }

    @Test
    void save() {
        String url = "testUrl";
        String response = "response";
        String params = "params";
        Integer code = 200;
        LocalDateTime date = LocalDateTime.now();

        RequestLogDto logDto = new RequestLogDto(null, date, url, params, code, response);

        RequestLog log = new RequestLog();
        log.setEndpoint(url);
        log.setCreatedAt(date);
        log.setResponse(response);
        log.setParameters(params);
        log.setStatusCode(code);

        when(requestLogRepository.save(log)).thenReturn(Mono.just(log));

        Mono<RequestLogDto> responseMono = requestLogService.save(logDto);

        StepVerifier
                .create(responseMono)
                .consumeNextWith(result -> {
                    assertNotNull(result);
                    assertEquals(log.getEndpoint(), result.endpoint());
                    assertEquals(log.getResponse(), result.response());
                    assertEquals(log.getParameters(), result.parameters());
                    assertEquals(log.getStatusCode(), result.statusCode());
                }).verifyComplete();
    }
}
