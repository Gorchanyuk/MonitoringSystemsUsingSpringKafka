package t1.gorchanyuk.metricsconsumer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import t1.gorchanyuk.metricsconsumer.dto.ErrorResponseDto;
import t1.gorchanyuk.metricsconsumer.exception.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleException(EntityNotFoundException e) {
        ErrorResponseDto response = ErrorResponseDto.builder()
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}