package com.study.export.excel.converter;



import com.study.export.dto.FieldDto;

import java.util.Objects;
import java.util.Set;

public class ObjectToBaseBoolean implements Converter<Object> {

    @Override
    public Object toTargetValue(FieldDto fieldConfig, Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof String) {
            if (fieldConfig.getValueMap() == null || fieldConfig.getValueMap().isEmpty()) {
                return Boolean.FALSE.booleanValue();
            }
            Set<String> keySet = fieldConfig.getValueMap().keySet();
            for (String K : keySet) {
                if (obj.equals(fieldConfig.getValueMap().get(K))) {
                    return Boolean.valueOf(K).booleanValue();
                }
            }
        }
        return Boolean.FALSE.booleanValue();
    }
}


