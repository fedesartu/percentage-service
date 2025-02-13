package org.fedesartu.percentage.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration

public class WebClientConfig {

    @Value("${app.providers.percentage-api.url:}")
    private String percentageApiUrl;

    @Bean(name = "percentage-api-client")
    public WebClient percentageApiClient() {
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(getConnectorProvider())))
                .baseUrl(percentageApiUrl)
                .build();
    }

    private ConnectionProvider getConnectorProvider() {
        return ConnectionProvider
                .builder("connection-provider")
                .maxConnections(10)
                .maxIdleTime(Duration.ofSeconds(5))
                .maxLifeTime(Duration.ofSeconds(5))
                .pendingAcquireTimeout(Duration.ofSeconds(5))
                .evictInBackground(Duration.ofSeconds(5))
                .build();
    }
}
