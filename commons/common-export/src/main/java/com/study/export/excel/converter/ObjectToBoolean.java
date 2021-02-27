package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

import java.util.Objects;
import java.util.Set;

public class ObjectToBoolean implements Converter<Object> {

    @Override
    public Boolean toTargetValue(FieldDto fieldConfig, Object obj) {
        if (Objects.isNull(obj)) {
            return Boolean.FALSE;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            if (fieldConfig.getValueMap() == null || fieldConfig.getValueMap().isEmpty()) {
                return Boolean.FALSE;
            }
            Set<String> keySet = fieldConfig.getValueMap().keySet();
            for (String K : keySet) {
                if (obj.equals(fieldConfig.getValueMap().get(K))) {
                    return Boolean.valueOf(K);
                }
            }
        }
        return Boolean.FALSE;
    }
}


