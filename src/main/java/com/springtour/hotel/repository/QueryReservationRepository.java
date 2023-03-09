package com.springtour.hotel.repository;

import com.springtour.hotel.dto.ReservationCreateRequest;

public interface QueryReservationRepository {

    long countByRoomAndHotel(ReservationCreateRequest request);
}
