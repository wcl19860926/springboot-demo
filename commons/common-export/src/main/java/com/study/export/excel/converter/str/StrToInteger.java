package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;

public class StrToInteger implements StrToObjectConverter {
    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (StringUtils.isBlank(obj)) {
            return 0;
        }
        return Integer.valueOf(Double.valueOf(obj).intValue());
    }
}
