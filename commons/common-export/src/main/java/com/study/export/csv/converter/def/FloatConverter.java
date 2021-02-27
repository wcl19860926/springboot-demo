package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

public class FloatConverter implements Converter<Float> {

    public FloatConverter() {
        super();
    }

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Float obj) {
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
    public Float stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Float.valueOf(value);
        }
        return null;
    }
}
