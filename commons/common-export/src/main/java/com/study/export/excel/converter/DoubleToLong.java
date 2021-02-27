package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;

public class DoubleToLong implements Converter<Double> {

    @Override
    public Long toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return Long.valueOf(obj.longValue());
    }
}
