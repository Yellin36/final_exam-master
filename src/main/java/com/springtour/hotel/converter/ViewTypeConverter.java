package com.springtour.hotel.converter;

import com.springtour.hotel.domain.ViewType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ViewTypeConverter implements AttributeConverter<ViewType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ViewType viewType) {
        return viewType.getDbValue();
    }

    @Override
    public ViewType convertToEntityAttribute(Integer integer) {
        return ViewType.fromDbValue(integer);
    }
}
