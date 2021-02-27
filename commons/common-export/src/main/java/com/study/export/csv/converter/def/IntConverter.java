package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

public class IntConverter implements Converter {


    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Object obj) {
        return String.valueOf(obj);
    }

    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    @Override
    public Object stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Integer.parseInt(value);
        }
        return 0;
    }
}
