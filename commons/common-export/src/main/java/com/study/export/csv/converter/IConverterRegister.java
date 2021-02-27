package com.study.export.csv.converter;

public interface IConverterRegister {

    /**
     * 注册类型转换器
     *
     * @param converter
     */
    void registerConverter(Converter converter);


}
