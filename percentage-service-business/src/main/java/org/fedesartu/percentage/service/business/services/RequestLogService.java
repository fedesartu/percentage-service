package org.fedesartu.percentage.service.business.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fedesartu.percentage.service.business.dto.RequestLogDto;
import org.fedesartu.percentage.service.business.mapper.RequestLogMapper;
import org.fedesartu.percentage.service.model.repository.postgres.RequestLogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public Flux<RequestLogDto> find(Integer page, Integer size, String url, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return requestLogRepository.findAllFiltered(url, dateFrom, dateTo, page, size).map(RequestLogMapper.INSTANCE::toDto);
    }

    public Mono<RequestLogDto> save(RequestLogDto requestLogDto) {
        return Mono.just(RequestLogMapper.INSTANCE.toEntity(requestLogDto))
                .flatMap(requestLogRepository::save)
                .map(RequestLogMapper.INSTANCE::toDto);
    }

}
