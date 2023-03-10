package com.springtour.hotel.controller;

import com.springtour.hotel.dto.ReservationCreateRequest;
import com.springtour.hotel.dto.ReservationCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.service.ReservationService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/hotel-api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationCreateResponse createReservation(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ReservationCreateRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        return reservationService.create(userId, request);
    }
}
