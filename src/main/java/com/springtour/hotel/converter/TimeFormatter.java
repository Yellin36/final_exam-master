package com.springtour.hotel.converter;

import java.time.LocalDateTime;

public interface TimeFormatter {

    String convert(LocalDateTime localDateTime);
}
