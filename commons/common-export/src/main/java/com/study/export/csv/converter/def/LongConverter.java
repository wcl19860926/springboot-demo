package com.study.export.csv.converter.def;

import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

public class LongConverter implements Converter<Long> {

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Long obj) {
        if (obj == null) {
            return "";
        }
        return String.valueOf(obj);
    }

    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    @Override
    public Long stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Long.valueOf(value);
        }
        return null;
    }
}
