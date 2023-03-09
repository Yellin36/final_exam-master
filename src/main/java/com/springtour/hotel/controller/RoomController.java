package com.springtour.hotel.controller;

import com.springtour.hotel.dto.RoomCreateRequest;
import com.springtour.hotel.dto.RoomCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.service.RoomService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/hotel-api/hotels/{hotel-id}/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomCreateResponse createHotelRoom(
            @PathVariable(name = "hotel-id") Long hotelId,
            @Valid @RequestBody RoomCreateRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        return roomService.create(hotelId, request);
    }

    @GetMapping
    public List<RoomResponse> getHotelRooms(@PathVariable(name = "hotel-id") Long hotelId) {
        return roomService.getRooms(hotelId);
    }
}
