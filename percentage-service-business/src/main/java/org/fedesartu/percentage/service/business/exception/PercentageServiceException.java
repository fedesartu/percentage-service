package org.fedesartu.percentage.service.business.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PercentageServiceException extends Exception {

    public PercentageServiceException(ExceptionCode exceptionCode, Object... params) {
        super(String.format(exceptionCode.getMessage(), params));
        httpStatus = exceptionCode.getStatus();
    }

    private HttpStatus httpStatus;

}
