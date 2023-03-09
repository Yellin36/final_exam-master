package com.springtour.hotel.exception;

import com.springtour.hotel.dto.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorResponse errorResponse;

    public ClientException(HttpStatus status, String errorMessage) {
        super(errorMessage);

        this.status = status;
        this.errorResponse = ErrorResponse.builder().errorMessage(errorMessage).build();
    }
}
