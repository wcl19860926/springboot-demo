package com.study.export.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto {
    //字段的名称
    private String fieldName;
    private String filedTitle;
    //导出的字段顺序
    private int order;
    //字段类型  java.lang.int
    private Class<?> type;
    private Method getMethod;
    private Method setMethod;
    //日期格试
    private String dataFormat;
    //配置的常量值的键值对
    private Map<String, String> valueMap;


}
