package org.fedesartu.percentage.service.business.services;

import lombok.RequiredArgsConstructor;
import org.fedesartu.percentage.service.business.client.PercentageClient;
import org.fedesartu.percentage.service.business.dto.CalculateDto;
import org.fedesartu.percentage.service.business.dto.CalculateResultDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final PercentageClient percentageClient;

    public Mono<CalculateResultDto> calculatePercentage(CalculateDto calculateDto) {
        return percentageClient.findPercentage().map(percentage -> {
            BigDecimal sum = BigDecimal.valueOf(calculateDto.numOne() + calculateDto.numTwo());
            BigDecimal percentageValue = sum.multiply(percentage.divide(BigDecimal.valueOf(100)));
            return new CalculateResultDto(percentageValue.add(sum).intValue());
        });
    }

}
