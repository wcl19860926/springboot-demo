package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;

public class StrToDouble implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (StringUtils.isBlank(obj)) {
            return 0.0;
        }
        return Double.valueOf(obj);
    }
}
