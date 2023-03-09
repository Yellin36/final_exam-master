package com.springtour.hotel.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtour.hotel.dto.ReservationCreateRequest;
import com.springtour.hotel.dto.ReservationCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.service.ReservationService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReservationService service;

    @Test
    @DisplayName("예약 실패 - 잘못된 요청인 경우")
    void createReservation_fail_invalidRequest() throws Exception {
        //given
        Long userId = 101L;
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 17);
        String hotelName = "";
        String roomName = "   ";
        ReservationCreateRequest request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
        String errorMessage = "잘못된 요청입니다.";

        //when
        ResultActions result = mockMvc.perform(post("/v1/hotel-api/users/{userId}", userId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", equalTo(errorMessage)));
    }

    @Test
    @DisplayName("예약 실패 - 예약 불가능한 사용자")
    void createReservation_fail_invalidUser() throws Exception {
        //given
        Long userId = 101L;
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 17);
        String hotelName = "FirstHotel";
        String roomName = "TwinRoom1";
        ReservationCreateRequest request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
        String errorMessage = "예약 불가능한 사용자입니다.";
        Mockito.when(service.create(eq(userId), any()))
                .thenThrow(new ClientException(HttpStatus.BAD_REQUEST, errorMessage));

        //when
        ResultActions result = mockMvc.perform(post("/v1/hotel-api/users/{userId}", userId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", equalTo(errorMessage)));
    }

    @Test
    @DisplayName("예약 실패 - 최대 예약 개수를 초과한 경우")
    void createReservation_fail_alreadyReservedUpToLimit() throws Exception {
        //given
        Long userId = 101L;
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 17);
        String hotelName = "FirstHotel";
        String roomName = "TwinRoom1";
        ReservationCreateRequest request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
        String errorMessage = "하루 최대 3개까지만 예약가능합니다.";
        Mockito.when(service.create(eq(userId), any()))
                .thenThrow(new ClientException(HttpStatus.BAD_REQUEST, errorMessage));

        //when
        ResultActions result = mockMvc.perform(post("/v1/hotel-api/users/{userId}", userId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", equalTo(errorMessage)));
    }

    @Test
    @DisplayName("예약 실패 - 예약 불가능한 객실인 경우")
    void createReservation_fail_alreadyReservedRoom() throws Exception {
        //given
        Long userId = 101L;
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 17);
        String hotelName = "FirstHotel";
        String roomName = "TwinRoom1";
        ReservationCreateRequest request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
        String errorMessage = "이미 예약된 객실입니다..";
        Mockito.when(service.create(eq(userId), any()))
                .thenThrow(new ClientException(HttpStatus.BAD_REQUEST, errorMessage));

        //when
        ResultActions result = mockMvc.perform(post("/v1/hotel-api/users/{userId}", userId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", equalTo(errorMessage)));
    }

    @Test
    @DisplayName("예약 성공")
    void createReservation() throws Exception {
        //given
        Long userId = 100L;
        LocalDate startDate = LocalDate.of(2025, 2, 15);
        LocalDate endDate = LocalDate.of(2025, 2, 17);
        String hotelName = "FirstHotel";
        String roomName = "TwinRoom1";
        ReservationCreateRequest request = new ReservationCreateRequest(
                startDate,
                endDate,
                hotelName,
                roomName
        );
        ReservationCreateResponse response = new ReservationCreateResponse(
                userId,
                startDate,
                endDate,
                hotelName,
                roomName
        );

        Mockito.when(service.create(eq(userId), any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(post("/v1/hotel-api/users/{userId}", userId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", equalTo(100)))
                .andExpect(jsonPath("$.startDate", equalTo(startDate.toString())))
                .andExpect(jsonPath("$.endDate", equalTo(endDate.toString())))
                .andExpect(jsonPath("$.hotelName", equalTo(hotelName)))
                .andExpect(jsonPath("$.roomName", equalTo(roomName)));
    }
}