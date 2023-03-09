package com.springtour.hotel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Room;
import com.springtour.hotel.domain.ViewType;
import com.springtour.hotel.dto.ReservationCreateRequest;
import com.springtour.hotel.dto.ReservationCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.repository.JpaHotelRepository;
import com.springtour.hotel.repository.JpaReservationRepository;
import com.springtour.hotel.repository.JpaRoomRepository;
import com.springtour.hotel.repository.QueryReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class ReservationServiceTest {

    ReservationService reservationService;

    JpaHotelRepository hotelRepository;
    JpaRoomRepository roomRepository;
    JpaReservationRepository reservationRepository;
    QueryReservationRepository queryReservationRepository;

    LocalDate startDate = LocalDate.of(2025, 2, 15);
    LocalDate endDate = LocalDate.of(2025, 2, 17);
    String hotelName = "FirstHotel";
    String roomName = "TwinRoom1";
    ReservationCreateRequest request;

    @BeforeEach
    void setUp() {
        hotelRepository = Mockito.mock(JpaHotelRepository.class);
        roomRepository = Mockito.mock(JpaRoomRepository.class);
        reservationRepository = Mockito.mock(JpaReservationRepository.class);
        queryReservationRepository = Mockito.mock(QueryReservationRepository.class);

        reservationService = new ReservationService(
                hotelRepository,
                roomRepository,
                reservationRepository,
                queryReservationRepository
        );

        request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
    }

    @Test
    @DisplayName("예약 생성 실패 - 예약 불가능한 사용자")
    void create_fail_invalidUser() {
        //given
        long userId = 101;

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, never()).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, never()).countByRoomAndHotel(any());
        verify(hotelRepository, never()).findByName(any());
        verify(roomRepository, never()).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 하루 최대 예약 가능 횟수 초과한 사용자")
    void create_fail_alreadyReservedUpToLimit() {
        //given
        long userId = 100;

        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenThrow(new ClientException(HttpStatus.BAD_REQUEST, "하루 최대 3개까지만 예약가능합니다."));

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, never()).countByRoomAndHotel(any());
        verify(hotelRepository, never()).findByName(any());
        verify(roomRepository, never()).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 과거의 객실을 예약하는 경우")
    void create_fail_invalidDate() {
        //given
        long userId = 100;
        ReservationCreateRequest request = new ReservationCreateRequest(
                LocalDate.of(2020, 1, 1),
                endDate,
                hotelName,
                roomName
        );
        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenReturn(1);

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, never()).countByRoomAndHotel(any());
        verify(hotelRepository, never()).findByName(any());
        verify(roomRepository, never()).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 이미 예약된 객실인 경우")
    void create_fail_alreadyReservedRoom() {
        //given
        long userId = 100;

        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenReturn(1);
        Mockito.when(queryReservationRepository.countByRoomAndHotel(any())).thenReturn(3L);

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, times(1)).countByRoomAndHotel(any());
        verify(hotelRepository, never()).findByName(any());
        verify(roomRepository, never()).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 존재하지 않는 호텔 경우")
    void create_fail_hotelNotFound() {
        //given
        long userId = 100;

        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenReturn(1);
        Mockito.when(queryReservationRepository.countByRoomAndHotel(any())).thenReturn(0L);
        Mockito.when(hotelRepository.findByName(any()))
                .thenThrow(new ClientException(HttpStatus.CONFLICT, "존재하지 않는 호텔입니다."));

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, times(1)).countByRoomAndHotel(any());
        verify(hotelRepository, times(1)).findByName(any());
        verify(roomRepository, never()).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 실패 - 존재하지 않는 객실인 경우")
    void create_fail_roomNotFound() {
        //given
        long userId = 100;

        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenReturn(1);
        Mockito.when(queryReservationRepository.countByRoomAndHotel(any())).thenReturn(0L);
        Mockito.when(hotelRepository.findByName(any()))
                .thenReturn(Optional.ofNullable(Hotel.builder()
                        .hotelId(100L)
                        .name("Hotel")
                        .build()));
        Mockito.when(roomRepository.findByName(any()))
                .thenThrow(new ClientException(HttpStatus.CONFLICT, "존재하지 않는 객실입니다."));

        //when, then
        assertThatThrownBy(() -> reservationService.create(userId, request)).isInstanceOf(
                ClientException.class);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, times(1)).countByRoomAndHotel(any());
        verify(hotelRepository, times(1)).findByName(any());
        verify(roomRepository, times(1)).findByName(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("예약 생성 성공")
    void create_success() {
        //given
        long userId = 100;

        Mockito.when(reservationRepository.countByUserIdAndCreatedAt(eq(userId), any()))
                .thenReturn(1);
        Mockito.when(queryReservationRepository.countByRoomAndHotel(any())).thenReturn(0L);
        Mockito.when(hotelRepository.findByName(any()))
                .thenReturn(Optional.ofNullable(Hotel.builder()
                        .hotelId(100L)
                        .name("Hotel")
                        .build()));
        Mockito.when(roomRepository.findByName(any())).thenReturn(Optional.ofNullable(
                Room.builder()
                        .roomId(100L)
                        .name("Hotel")
                        .capacity(1)
                        .floor(1)
                        .bathtubFlag(true)
                        .viewType(ViewType.CITY)
                        .createdAt(LocalDateTime.now())
                        .build()
        ));

        //when
        ReservationCreateResponse response = reservationService.create(userId, request);

        //then
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getStartDate()).isEqualTo(startDate);
        assertThat(response.getEndDate()).isEqualTo(endDate);
        assertThat(response.getHotelName()).isEqualTo(hotelName);
        assertThat(response.getRoomName()).isEqualTo(roomName);

        verify(reservationRepository, times(1)).countByUserIdAndCreatedAt(any(), any());
        verify(queryReservationRepository, times(1)).countByRoomAndHotel(any());
        verify(hotelRepository, times(1)).findByName(any());
        verify(roomRepository, times(1)).findByName(any());
        verify(reservationRepository, times(1)).save(any());
    }
}