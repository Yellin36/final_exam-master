package com.springtour.hotel.repository;

import static org.assertj.core.api.Assertions.*;

import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Reservation;
import com.springtour.hotel.domain.Room;
import com.springtour.hotel.domain.ViewType;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaReservationRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaReservationRepository repository;

    Reservation reservation;

    Hotel hotel;
    Room room;
    @BeforeEach
    void setUp() {
        LocalDate startDate = LocalDate.now().plusDays(7);
        LocalDate endDate = startDate.plusDays(3);

        hotel = Hotel.builder().name("TestHotel").build();
        em.persist(hotel);

        room = Room.builder()
                .hotel(hotel)
                .name("TestRoom")
                .capacity(1)
                .floor(1)
                .bathtubFlag(true)
                .viewType(ViewType.OCEAN)
                .createdAt(LocalDateTime.now()).build();
        em.persist(room);

        reservation = getReservation(startDate, endDate, hotel, room);
    }

    @Test
    void save() {
    }

    @Test
    void countByUserIdAndCreatedAt() {
    }

    private Reservation getReservation(
            LocalDate startDate,
            LocalDate endDate,
            Hotel hotel,
            Room room
    ) {
        return Reservation.builder()
                .userId(100L)
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDate.now())
                .hotel(hotel)
                .room(room).build();
    }
}