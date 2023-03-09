package com.springtour.hotel.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private String name;
    private int capacity;
    private int floor;
    private boolean hasBathtub;
    private String viewType;
    private String createdAt;
}
