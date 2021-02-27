package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;

public class DoubleToFloat implements Converter<Double> {

    @Override
    public Float toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return Float.valueOf(obj.floatValue());
    }
}
