package com.springtour.hotel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.springtour.hotel.controller.RoomResponse;
import com.springtour.hotel.converter.TimeFormatter;
import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Room;
import com.springtour.hotel.domain.ViewType;
import com.springtour.hotel.repository.JpaHotelRepository;
import com.springtour.hotel.repository.JpaRoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("dev")
@Disabled
public class RoomServiceTest {
    RoomService roomService;

    TimeFormatter timeFormatter;
    JpaHotelRepository hotelRepository;
    JpaRoomRepository roomRepository;

    String pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    @BeforeEach
    void setUp() {
        timeFormatter = Mockito.mock(TimeFormatter.class);
        hotelRepository = Mockito.mock(JpaHotelRepository.class);
        roomRepository = Mockito.mock(JpaRoomRepository.class);

        roomService = new RoomService(timeFormatter, hotelRepository, roomRepository);
    }

    @Test
    void getRooms() {
        //given
        Long hotelId = 101L;

        Hotel hotel = Hotel.builder().hotelId(1L).name("FirstHotel").build();
        List<Room> roomList = getRoomList(hotel);

        Mockito.when(hotelRepository.findByHotelId(hotelId)).thenReturn(Optional.ofNullable(hotel));
        Mockito.when(roomRepository.findByHotel(any())).thenReturn(roomList);

        //when
        List<RoomResponse> result = roomService.getRooms(hotelId);

        //then
        result.forEach(x -> System.out.println(x.getCreatedAt()));
        assertThat(Pattern.matches(pattern, result.get(0).getCreatedAt()));
    }

    List<Room> getRoomList(Hotel hotel) {
        List<Room> roomList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            roomList.add(Room.builder()
                    .roomId((long) i)
                    .hotel(hotel)
                    .name("Room" + i)
                    .capacity(i + 1)
                    .floor(i + 1)
                    .bathtubFlag(true)
                    .viewType(ViewType.OCEAN)
                    .createdAt(LocalDateTime.now().minusDays(i))
                    .build());
        }
        return roomList;
    }
}