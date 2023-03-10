package com.springtour.hotel.converter;

import com.springtour.hotel.config.DateTimeProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DevTimeFormatter implements TimeFormatter {

    private final DateTimeProperties properties;

    @Override
    public String convert(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(properties.getDev()));
    }
}
