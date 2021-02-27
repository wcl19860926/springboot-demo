package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.math.BigDecimal;
import java.util.Objects;

public class DoubleToBigDecimal implements Converter<Double> {

    @Override
    public BigDecimal toTargetValue(FieldDto fieldConfig, Double obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return new BigDecimal(obj);
    }
}
