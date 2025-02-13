package org.fedesartu.percentage.service.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

@RequiredArgsConstructor
@Configuration
@EnableR2dbcRepositories(basePackages = "org.fedesartu.percentage.service.model.repository.postgres")
@EntityScan("org.fedesartu.percentage.service.model.entity.postgres")
public class PercentageDataSourceConfig {

    private final R2dbcProperties dataSourceProperties;

    @Bean
    @Primary
    @Qualifier(value = "percentageConnectionFactory")
    public ConnectionFactory percentageConnectionFactory() {
        return ConnectionFactoryBuilder
                .withUrl(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
    }

    @Bean
    public R2dbcEntityOperations percentageEntityTemplate(@Qualifier("percentageConnectionFactory") ConnectionFactory percentageConnectionFactory) {
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
        DatabaseClient databaseClient = DatabaseClient.builder()
                .connectionFactory(percentageConnectionFactory)
                .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
                .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }

}
