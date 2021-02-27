package com.study.export.excel.converter.str;


import com.study.export.dto.FieldDto;

public interface StrToObjectConverter {

    Object toTargetValue(FieldDto fieldConfig, String obj);

}
