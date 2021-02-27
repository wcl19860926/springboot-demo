package com.study.export.csv.converter;


import com.study.export.csv.converter.def.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DefaultConverterHolder extends AbstractConvertHolder {
    private Map<String, Converter> defaultConverterMap;

    public DefaultConverterHolder() {
        defaultConverterMap = new HashMap<>();
        defaultConverterMap.put(Date.class.getTypeName(), new DateConverter());
        defaultConverterMap.put(Long.class.getTypeName(), new LongConverter());
        defaultConverterMap.put(long.class.getTypeName(), new BaseLongConverter());
        defaultConverterMap.put(Integer.class.getTypeName(), new IntegerConverter());
        defaultConverterMap.put(int.class.getTypeName(), new IntConverter());
        defaultConverterMap.put(Boolean.class.getTypeName(), new BooleanConverter());
        defaultConverterMap.put(boolean.class.getTypeName(), new BaseBooleanConverter());
        defaultConverterMap.put(Double.class.getTypeName(), new DoubleConverter());
        defaultConverterMap.put(Float.class.getTypeName(), new FloatConverter());
        defaultConverterMap.put(float.class.getTypeName(), new BaseFloatConverter());
        defaultConverterMap.put(double.class.getTypeName(), new BaseDoubleConverter());
        defaultConverterMap.put(BigDecimal.class.getTypeName(), new BigDecimalConverter());


        super.addConverter(this.defaultConverterMap);
    }


}
