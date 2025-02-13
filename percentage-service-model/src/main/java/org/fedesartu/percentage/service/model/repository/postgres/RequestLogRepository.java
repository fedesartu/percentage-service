package org.fedesartu.percentage.service.model.repository.postgres;

import org.fedesartu.percentage.service.model.entity.postgres.RequestLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface RequestLogRepository extends ReactiveCrudRepository<RequestLog, Integer> {

    @Query("SELECT rl.* " //
            + "FROM logs.request_log rl " //
            + "WHERE (:url IS NULL OR rl.endpoint = :url) " //
            + "AND (:dateFrom IS NULL OR :dateFrom IS NULL OR rl.created_at BETWEEN :dateFrom and :dateTo)" //
            + "LIMIT :size OFFSET :page ")
    Flux<RequestLog> findAllFiltered(String url, LocalDateTime dateFrom, LocalDateTime dateTo, Integer page, Integer size);

}
