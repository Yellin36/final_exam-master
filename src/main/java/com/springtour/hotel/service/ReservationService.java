package com.springtour.hotel.service;

import com.springtour.hotel.controller.UserAuthzValidator;
import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Reservation;
import com.springtour.hotel.domain.Room;
import com.springtour.hotel.dto.ReservationCreateRequest;
import com.springtour.hotel.dto.ReservationCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.repository.JpaHotelRepository;
import com.springtour.hotel.repository.JpaReservationRepository;
import com.springtour.hotel.repository.JpaRoomRepository;
import com.springtour.hotel.repository.QueryReservationRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final JpaHotelRepository hotelRepository;
    private final JpaRoomRepository roomRepository;
    private final JpaReservationRepository reservationRepository;
    private final QueryReservationRepository queryReservationRepository;

    public ReservationCreateResponse create(Long userId, ReservationCreateRequest request) {
        checkValidUser(userId);
        checkValidDate(request);

        Reservation reservation = createReservation(userId, request);

        reservationRepository.save(reservation);

        return new ReservationCreateResponse(
                userId,
                request.getStartDate(),
                request.getEndDate(),
                request.getHotelName(),
                request.getRoomName()
        );
    }

    private Reservation createReservation(Long userId, ReservationCreateRequest request) {
        Hotel hotel = tryGetHotelByName(request.getHotelName());
        Room room = tryGetRoomByName(request.getRoomName());

        return Reservation.builder()
                .userId(userId)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDate.now())
                .hotel(hotel)
                .room(room)
                .build();
    }

    private Room tryGetRoomByName(String name) {
        return roomRepository.findByName(name)
                .orElseThrow(() -> new ClientException(HttpStatus.NOT_FOUND, "???????????? ?????? ???????????????."));
    }

    private Hotel tryGetHotelByName(String name) {
        return hotelRepository.findByName(name)
                .orElseThrow(() -> new ClientException(HttpStatus.NOT_FOUND, "???????????? ?????? ???????????????."));
    }

    private void checkValidUser(Long userId) {
        if (!UserAuthzValidator.isValid(userId)) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "?????? ???????????? ??????????????????.");
        }
        if (reservationRepository.countByUserIdAndCreatedAt(userId, LocalDate.now()) == 3) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "?????? ?????? 3???????????? ?????????????????????.");
        }
    }

    private void checkValidDate(ReservationCreateRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate()) ||
                request.getStartDate().isBefore(LocalDate.now())) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "????????? ???????????????.");
        }
        if (queryReservationRepository.countByRoomAndHotel(request) > 0) {
            throw new ClientException(HttpStatus.CONFLICT, "?????? ????????? ???????????????.");
        }
    }
}
