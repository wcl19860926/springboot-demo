package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;

import java.util.Objects;
import java.util.Set;

public class StrToBaseBoolean implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (fieldConfig.getValueMap() == null || fieldConfig.getValueMap().isEmpty()) {
            return Boolean.FALSE.booleanValue();
        }
        Set<String> keySet = fieldConfig.getValueMap().keySet();
        for (String K : keySet) {
            if (obj.equals(fieldConfig.getValueMap().get(K))) {
                return Boolean.valueOf(K).booleanValue();
            }
        }
        if ("true".equalsIgnoreCase(obj)) {
            return Boolean.TRUE.booleanValue();
        }
        if ("false".equalsIgnoreCase(obj)) {
            return Boolean.FALSE.booleanValue();
        }
        return Boolean.FALSE.booleanValue();
    }
}
