package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

public class DoubleConverter implements Converter<Double> {

    public DoubleConverter() {
        super();
    }

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Double obj) {
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
    public Double stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.valueOf(value);
        }
        return null;
    }
}
