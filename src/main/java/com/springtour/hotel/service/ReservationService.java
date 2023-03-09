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
                .orElseThrow(() -> new ClientException(HttpStatus.NOT_FOUND, "존재하지 않는 객실입니다."));
    }

    private Hotel tryGetHotelByName(String name) {
        return hotelRepository.findByName(name)
                .orElseThrow(() -> new ClientException(HttpStatus.NOT_FOUND, "존재하지 않는 호텔입니다."));
    }

    private void checkValidUser(Long userId) {
        if (!UserAuthzValidator.isValid(userId)) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "예약 불가능한 사용자입니다.");
        }
        if (reservationRepository.countByUserIdAndCreatedAt(userId, LocalDate.now()) == 3) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "하루 최대 3개까지만 예약가능합니다.");
        }
    }

    private void checkValidDate(ReservationCreateRequest request) {
        if (request.getEndDate().isBefore(request.getStartDate()) ||
                request.getStartDate().isBefore(LocalDate.now())) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }
        if (queryReservationRepository.countByRoomAndHotel(request) > 0) {
            throw new ClientException(HttpStatus.CONFLICT, "이미 예약된 객실입니다.");
        }
    }
}
