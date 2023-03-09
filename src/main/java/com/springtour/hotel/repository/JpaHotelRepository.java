package com.springtour.hotel.repository;

import com.springtour.hotel.domain.Hotel;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface JpaHotelRepository extends Repository<Hotel, Long> {

    Optional<Hotel> findByHotelId(Long hotelId);

    Optional<Hotel> findByName(String name);
}
