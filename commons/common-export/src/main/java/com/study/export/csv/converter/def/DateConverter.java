package com.study.export.csv.converter.def;


import com.study.export.csv.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateConverter implements Converter<Date> {


    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /* */

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    @Override
    public String objectToString(Date obj) {
        if (Objects.isNull(obj)) {
            return "";
        }
        return format.format(obj);
    }

    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    @Override
    public Date stringToObject(String value) {
        if (!StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
