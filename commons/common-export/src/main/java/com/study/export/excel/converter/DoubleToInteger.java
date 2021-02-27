package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;

public class DoubleToInteger implements Converter<Double> {

    @Override
    public Integer toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return Integer.valueOf(obj.intValue());
    }
}
