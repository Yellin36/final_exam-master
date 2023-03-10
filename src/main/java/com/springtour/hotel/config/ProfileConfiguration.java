package com.springtour.hotel.config;

import com.springtour.hotel.converter.DevTimeFormatter;
import com.springtour.hotel.converter.RealTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfiguration {
    @Bean
    @Profile({"dev", "default"})
    DevTimeFormatter devTimeFormatter(DateTimeProperties dateTimeProperties) {
        return new DevTimeFormatter(dateTimeProperties);
    }

    @Bean
    @Profile("real")
    RealTimeFormatter realTimeFormatter(DateTimeProperties dateTimeProperties) {
        return new RealTimeFormatter(dateTimeProperties);
    }
}
