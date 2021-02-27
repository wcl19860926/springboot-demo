package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;

import java.util.Objects;
import java.util.Set;

public class StrToBoolean implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (Objects.isNull(obj)) {
            return Boolean.FALSE;
        }
        if (fieldConfig.getValueMap() == null || fieldConfig.getValueMap().isEmpty()) {
            return Boolean.FALSE;
        }
        Set<String> keySet = fieldConfig.getValueMap().keySet();
        for (String K : keySet) {
            if (obj.equals(fieldConfig.getValueMap().get(K))) {
                return Boolean.valueOf(K).booleanValue();
            }
        }
        if ("true".equalsIgnoreCase(obj)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(obj)) {
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

}
