package com.springtour.hotel.repository;

import com.springtour.hotel.domain.Reservation;
import java.time.LocalDate;
import org.springframework.data.repository.Repository;

public interface JpaReservationRepository extends Repository<Reservation, Long> {

    void save(Reservation reservation);
    int countByUserIdAndCreatedAt(Long userId, LocalDate createdAt);
}
