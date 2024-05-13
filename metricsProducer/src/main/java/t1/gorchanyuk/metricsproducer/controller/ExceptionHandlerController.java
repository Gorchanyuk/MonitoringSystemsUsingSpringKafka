package t1.gorchanyuk.metricsproducer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.gorchanyuk.metricsproducer.exception.SendMetricException;
import t1.gorchanyuk.metricsproducer.model.error.ErrorResponseDto;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(SendMetricException e) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}