package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;

public class DoubleToShort implements Converter<Double> {

    @Override
    public Short toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return Short.valueOf(obj.shortValue());
    }
}
