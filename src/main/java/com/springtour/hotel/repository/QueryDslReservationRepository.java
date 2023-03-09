package com.springtour.hotel.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springtour.hotel.domain.querydsl.QReservation;
import com.springtour.hotel.dto.ReservationCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class QueryDslReservationRepository implements QueryReservationRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countByRoomAndHotel(ReservationCreateRequest request) {
        QReservation reservation = QReservation.reservation;

        return queryFactory.select(reservation.count())
                .from(reservation)
                .where(reservation.hotel.name.eq(request.getHotelName())
                        .and(reservation.room.name.eq(request.getRoomName()))
                        .and(reservation.startDate.between(request.getStartDate(), request.getEndDate())
                                .or(reservation.endDate.between(request.getStartDate(), request.getEndDate()))))
                .fetchOne();
    }
}
