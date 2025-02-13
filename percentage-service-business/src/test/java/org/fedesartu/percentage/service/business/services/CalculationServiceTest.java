package org.fedesartu.percentage.service.business.services;

import org.fedesartu.percentage.service.business.client.PercentageClient;
import org.fedesartu.percentage.service.business.dto.CalculateDto;
import org.fedesartu.percentage.service.business.dto.CalculateResultDto;
import org.fedesartu.percentage.service.business.exception.PercentageServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.fedesartu.percentage.service.business.exception.ExceptionCode.CANNOT_GET_PERCENTAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class CalculationServiceTest {

    @InjectMocks
    private CalculationService calculationService;

    @Mock
    private PercentageClient percentageClient;

    @Test
    void calculatePercentageValid() {
        CalculateDto calculateDto = new CalculateDto(1, 9);
        BigDecimal percentage = BigDecimal.valueOf(10);
        int expectedResult = 11;
        when(percentageClient.findPercentage()).thenReturn(Mono.just(percentage));

        Mono<CalculateResultDto> resultMono = calculationService.calculatePercentage(calculateDto);

        StepVerifier
                .create(resultMono)
                .consumeNextWith(result -> {
                    assertNotNull(result);
                    assertEquals(expectedResult, result.result());
                }).verifyComplete();

    }

    @Test
    void calculatePercentageFailedClient() {
        CalculateDto calculateDto = new CalculateDto(1, 9);
        when(percentageClient.findPercentage())
                .thenReturn(Mono.error(new PercentageServiceException(CANNOT_GET_PERCENTAGE, "Cannot find percentage")));

        Mono<CalculateResultDto> resultMono = calculationService.calculatePercentage(calculateDto);

        StepVerifier
                .create(resultMono)
                .expectError(PercentageServiceException.class)
                .verify();
    }
}
