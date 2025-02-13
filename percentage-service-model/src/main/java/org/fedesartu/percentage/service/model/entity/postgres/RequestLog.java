package org.fedesartu.percentage.service.model.entity.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "request_log", schema = "logs")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "endpoint")
    private String endpoint;

    @Lob
    @Column(name = "parameters")
    private String parameters;

    @Lob
    @Column(name = "response")
    private String response;

    @Column(name = "status_code")
    private Integer statusCode;

}
