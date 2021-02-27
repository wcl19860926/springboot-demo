package com.study.export.csv.converter;

public interface Converter<Object> {

    /**
     * 将对象转换为String
     *
     * @param obj
     * @return
     */
    String objectToString(Object obj);


    /**
     * 将String转为对象
     *
     * @param value
     * @return
     */
    Object stringToObject(String value);

}
