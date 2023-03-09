package com.springtour.hotel.service;

import com.springtour.hotel.controller.RoomResponse;
import com.springtour.hotel.converter.TimeFormatter;
import com.springtour.hotel.domain.Hotel;
import com.springtour.hotel.domain.Room;
import com.springtour.hotel.domain.ViewType;
import com.springtour.hotel.dto.RoomCreateRequest;
import com.springtour.hotel.dto.RoomCreateResponse;
import com.springtour.hotel.exception.ClientException;
import com.springtour.hotel.repository.JpaHotelRepository;
import com.springtour.hotel.repository.JpaRoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    private final TimeFormatter timeFormatter;
    private final JpaHotelRepository hotelRepository;
    private final JpaRoomRepository roomRepository;

    @Transactional
    public RoomCreateResponse create(Long hotelId, RoomCreateRequest request) {
        Hotel hotel = tryGetHotelByHotelId(hotelId);

        checkValidRoomName(request, hotel);

        Room room = Room.builder()
                .hotel(hotel)
                .name(request.getName())
                .capacity(request.getCapacity())
                .floor(request.getFloor())
                .bathtubFlag(request.getHasBathtub())
                .viewType(getValidViewType(request))
                .createdAt(LocalDateTime.now()).build();

        roomRepository.save(room);

        return RoomCreateResponse.builder().id(room.getRoomId()).build();
    }

    private void checkValidRoomName(RoomCreateRequest request, Hotel hotel) {
        if (roomRepository.existsByHotelAndName(hotel, request.getName())) {
            throw new ClientException(HttpStatus.CONFLICT, "이미 존재하는 객실입니다.");
        }
    }

    private ViewType getValidViewType(RoomCreateRequest request) {
        ViewType viewType = ViewType.fromParameter(request.getViewType());

        if (Objects.isNull(viewType)) {
            throw new ClientException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        return viewType;
    }

    // RoomResponse 클래스는 Room Entity 객체를 클라이언트에게 응답하기 위한 DTO 입니다.
    // 객실 정보 조회 API 명세서의 Response 양식을 보시고 적절한 형태로 RoomResponse 클래스를 만들어주세요.
    // JSON message 의 viewType 속성은 미리 제공한 ViewType enum의 parameter 값을 사용해야 합니다.
    // Hint. javax.persistence.AttributeConverter 인터페이스와 @Convert 애너테이션을 사용하면 됩니다.
    @Transactional(readOnly = true)
    public List<RoomResponse> getRooms(Long hotelId) {
        Hotel hotel = tryGetHotelByHotelId(hotelId);
        List<Room> roomList = roomRepository.findByHotel(hotel);

        return roomList.stream()
                .map(room -> new RoomResponse(
                        room.getRoomId(),
                        room.getName(),
                        room.getCapacity(),
                        room.getFloor(),
                        room.isBathtubFlag(),
                        room.getViewType().getParameter(),
                        timeFormatter.convert(room.getCreatedAt())
                )).collect(Collectors.toList());
    }

    private Hotel tryGetHotelByHotelId(Long hotelId) {
        return hotelRepository.findByHotelId(hotelId)
                .orElseThrow(() -> new ClientException(HttpStatus.NOT_FOUND, "존재하지 않는 호텔입니다."));
    }
}
