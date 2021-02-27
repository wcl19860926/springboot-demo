package com.study.export.csv.converter;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractConvertHolder implements IConverterHolder {


    private Map<String, Converter> converterMap = new HashMap<>();


    /**
     * 添加converTer
     *
     * @param converter
     */
    @Override
    public void addConverter(Converter converter) {
        this.add(converter);

    }


    private void add(Converter converter) {
        if (!Objects.isNull(converter)) {
            Type[] types = converter.getClass().getGenericInterfaces();
            if (types != null) {
                ParameterizedType parameterizedType = null;
                for (Type type : types) {
                    if (type instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType) type;
                        if (Converter.class == parameterizedType.getRawType()) {
                            converterMap.put(parameterizedType.getActualTypeArguments()[0].getTypeName(), converter);
                        }
                    }
                }
            }
        }
    }


    /**
     * 判断是否含有某种类型的converter
     *
     * @param cls
     * @return
     */
    @Override
    public <T> boolean contain(Class<T> cls) {
        return converterMap.containsKey(cls.getTypeName());
    }

    @Override
    public Converter getConverter(String typeName) {
        return (Converter) converterMap.get(typeName);
    }


    /**
     * 得到所有的converter
     *
     * @return
     */
    @Override
    public Map<String, Converter> getConverters() {
        return this.converterMap;
    }

    /**
     * 添加Converter
     *
     * @param converterMap
     */
    @Override
    public void addConverter(Map<String, Converter> converterMap) {
        if (!CollectionUtils.isEmpty(converterMap)) {
            this.converterMap.putAll(converterMap);
        }
    }
}
