package org.fedesartu.percentage.service.controller;

import lombok.RequiredArgsConstructor;
import org.fedesartu.percentage.service.business.dto.CalculateDto;
import org.fedesartu.percentage.service.business.dto.CalculateResultDto;
import org.fedesartu.percentage.service.business.services.CalculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/calculator")
public class CalculatorController {

    private final CalculationService calculationService;

    @PostMapping
    public Mono<CalculateResultDto> calculatePercentage(@RequestBody CalculateDto calculateDto) {
        return calculationService.calculatePercentage(calculateDto);
    }

}
