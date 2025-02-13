package org.fedesartu.percentage.service.handler;

import org.fedesartu.percentage.service.business.exception.ErrorResponseDto;
import org.fedesartu.percentage.service.business.exception.PercentageServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = PercentageServiceException.class)
    public final ResponseEntity<ErrorResponseDto> handlePercentageServiceException(PercentageServiceException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getHttpStatus().value(), e.getMessage());
        return new ResponseEntity<>(errorResponseDto, e.getHttpStatus());
    }

}
