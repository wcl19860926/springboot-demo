package com.study.export.csv.converter;

import java.util.Map;

public interface IConverterHolder {

    /**
     * 添加converTer
     *
     * @param converter
     */
    void addConverter(Converter converter);


    /**
     * 判断是否含有某种类型的converter
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> boolean contain(Class<T> cls);

    /**
     * 得到所有的converter
     *
     * @return
     */
    Map<String, Converter> getConverters();

    /**
     * 根据某中类型得到某种converter
     *
     * @param typeName
     * @param
     * @return
     */
    Converter getConverter(String typeName);

    /**
     * 添加Converter
     *
     * @param converterMap
     */
    void addConverter(Map<String, Converter> converterMap);
}
