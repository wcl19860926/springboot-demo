package com.study.export.excel.converter;


import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;

import java.util.Set;

public class StringToString implements Converter<String> {


    /**
     * 主要是用于，某些有特定值 ，且在com.biz.export.annotation.ExportField
     * keyValue配置了值字段
     *
     * @param fieldConfig
     * @param obj
     * @return
     */
    @Override
    public String toTargetValue(FieldDto fieldConfig, String obj) {
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
