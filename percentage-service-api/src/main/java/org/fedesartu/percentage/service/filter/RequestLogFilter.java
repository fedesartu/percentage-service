package org.fedesartu.percentage.service.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fedesartu.percentage.service.business.dto.RequestLogDto;
import org.fedesartu.percentage.service.business.services.RequestLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@Order(-16)
@RequiredArgsConstructor
public class RequestLogFilter implements WebFilter {

    private final RequestLogService requestLogService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getURI().getPath().contains("api/")) {
            return chain.filter(exchange);
        }

        ServerHttpRequestWrapper requestWrapper = new ServerHttpRequestWrapper(exchange.getRequest());
        ServerHttpResponseWrapper responseWrapper = new ServerHttpResponseWrapper(exchange.getResponse());

        return chain.filter(exchange.mutate().request(requestWrapper).response(responseWrapper).build())
                .doFinally((a) -> {
                    RequestLogDto log = new RequestLogDto(null, LocalDateTime.now(),
                            exchange.getRequest().getURI().toString(),
                            requestWrapper.getCachedBody(),
                            exchange.getResponse().getStatusCode().value(),
                            responseWrapper.getCachedBody());
                    requestLogService.save(log).subscribe();
                });
    }
}

