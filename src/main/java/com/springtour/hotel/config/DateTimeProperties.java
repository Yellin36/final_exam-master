package com.springtour.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "date-format")
public class DateTimeProperties {

    private String dev;
    private String real;

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public String getDev() {
        return dev;
    }

    public String getReal() {
        return real;
    }
}
