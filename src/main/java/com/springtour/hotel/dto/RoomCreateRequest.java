package com.springtour.hotel.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {

    @NotBlank
    @Length(max = 100)
    private String name;

    @Range(max = 255)
    private int capacity;
    @Range(max = 255)
    private int floor;
    @NotNull
    private Boolean hasBathtub;
    @NotBlank
    private String viewType;
}
