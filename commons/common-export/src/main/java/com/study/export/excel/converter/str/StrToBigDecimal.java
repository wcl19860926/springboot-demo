package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;

import java.math.BigDecimal;

public class StrToBigDecimal implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        return new BigDecimal(obj);
    }
}
