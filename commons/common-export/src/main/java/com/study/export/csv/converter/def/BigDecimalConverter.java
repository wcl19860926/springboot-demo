package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalConverter implements Converter<BigDecimal> {

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(BigDecimal obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        return obj.toPlainString();
    }

    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    @Override
    public BigDecimal stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return new BigDecimal(value);
        }
        return null;
    }
}
