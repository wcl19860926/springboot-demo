package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class BooleanConverter implements Converter<Boolean> {

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Boolean obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        return obj ? "true" : "false";
    }

    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    @Override
    public Boolean stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Boolean.valueOf(value);
        }
        return null;
    }
}
