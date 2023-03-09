package com.springtour.hotel.advice;

import com.springtour.hotel.dto.ErrorResponse;
import com.springtour.hotel.exception.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = ClientException.class)
    public ResponseEntity<ErrorResponse> clientExceptionHandler(ClientException ce) {
        log.error("{}", ce.getMessage());
        return ResponseEntity.status(ce.getStatus()).body(ce.getErrorResponse());
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(Exception e) {
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().errorMessage("시스템에서 에러가 발생했습니다.").build());
    }
}
