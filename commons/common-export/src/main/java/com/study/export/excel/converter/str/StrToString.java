package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;

import java.util.Set;

public class StrToString implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (StringUtils.isBlank(obj)) {
            return obj;
        }
        obj = obj.trim();
        if (fieldConfig.getValueMap() == null || fieldConfig.getValueMap().isEmpty()) {
            return obj;
        }
        Set<String> keySet = fieldConfig.getValueMap().keySet();
        for (String K : keySet) {
            if (obj.equals(fieldConfig.getValueMap().get(K))) {
                return K;
            }
        }
        return null;
    }
}
