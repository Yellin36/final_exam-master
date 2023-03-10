package com.springtour.hotel.repository;

import static org.assertj.core.api.Assertions.*;

import com.springtour.hotel.domain.Hotel;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaHotelRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaHotelRepository hotelRepository;

    Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder().name("First-Hotel").build();

        em.persist(hotel);
    }

    @Test
    void findByHotelId() {
        //given
        Long hotelId = hotel.getHotelId();

        //when
        Optional<Hotel> result = hotelRepository.findByHotelId(hotelId);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getHotelId()).isEqualTo(hotelId);
    }

    @Test
    void findByName() {
        //given
        String name = hotel.getName();

        //when
        Optional<Hotel> result = hotelRepository.findByName(name);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(name);
    }
}