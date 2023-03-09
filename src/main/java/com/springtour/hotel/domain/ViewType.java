package com.springtour.hotel.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum ViewType {

    CITY(0, "cityView"),
    OCEAN(1, "oceanView"),
    MOUNTAIN(2, "mountainView");

    public static Map<Integer, ViewType> dbValueMap = Arrays.stream(ViewType.values())
            .collect(Collectors.toMap(ViewType::getDbValue, Function.identity()));
    public static Map<String, ViewType> parameterMap = Arrays.stream(ViewType.values())
            .collect(Collectors.toMap(ViewType::getParameter, Function.identity()));
    private int dbValue;
    private String parameter;

    ViewType(int dbValue, String parameter) {
        this.dbValue = dbValue;
        this.parameter = parameter;
    }

    public static ViewType fromDbValue(Integer dbValue) {
        return dbValueMap.get(dbValue);
    }

    public static ViewType fromParameter(String parameter) {
        return parameterMap.get(parameter);
    }
}
