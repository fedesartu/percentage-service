package org.fedesartu.percentage.service.business.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error, please contact admin."),
    CANNOT_GET_PERCENTAGE(HttpStatus.SERVICE_UNAVAILABLE, "Cannot find percentage."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests for endpoint.");

    private final HttpStatus status;

    private final String message;
}
