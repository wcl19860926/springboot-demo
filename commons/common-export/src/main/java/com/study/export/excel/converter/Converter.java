package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;

public interface Converter<T> {

    <V> V toTargetValue(FieldDto fieldConfig, T obj);

}
