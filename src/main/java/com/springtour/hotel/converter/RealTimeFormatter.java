package com.springtour.hotel.converter;

import com.springtour.hotel.config.DateTimeProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("real")
public class RealTimeFormatter implements TimeFormatter{

    private final DateTimeProperties properties;
    @Override
    public String convert(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(properties.getReal()));
    }
}
