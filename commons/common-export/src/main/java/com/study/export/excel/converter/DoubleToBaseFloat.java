package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;

public class DoubleToBaseFloat implements Converter<Double> {

    @Override
    public Object toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return 0.0;
        }
        return obj.floatValue();
    }
}
