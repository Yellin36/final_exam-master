package com.springtour.hotel.repository;

import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface JpaRoomRepository extends Repository<Room, Long> {

    void save(Room room);

    boolean existsByHotelAndName(Hotel hotel, String name);

    Optional<Room> findByName(String roomName);

    List<Room> findByHotel(Hotel hotel);
}
