package org.fedesartu.percentage.service.controller;

import lombok.RequiredArgsConstructor;
import org.fedesartu.percentage.service.business.dto.RequestLogDto;
import org.fedesartu.percentage.service.business.services.RequestLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/request-log")
public class RequestLogController {

    private final RequestLogService requestLogService;

    @GetMapping
    public Flux<RequestLogDto> find(@RequestParam(required = false, defaultValue = "0") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String url,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo) {
        return requestLogService.find(page, size, url, dateFrom, dateTo);
    }
}
