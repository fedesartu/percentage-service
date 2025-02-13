package org.fedesartu.percentage.service.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RequestLogDto(Integer id, @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt, String endpoint,
                            String parameters, Integer statusCode, String response) {
}
