package com.study.export.csv.io;

import com.study.export.csv.converter.Converter;
import com.study.export.csv.converter.DefaultConverterHolder;
import com.study.export.csv.converter.IConverterHolder;
import com.study.export.csv.converter.IConverterRegister;
import com.study.export.dto.FieldDto;
import org.apache.log4j.Logger;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanInterfaceProxy;
import org.supercsv.util.MethodCache;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvBeanObjectReader<T> extends AbstractCsvReader implements ICsvBeanObjectReader, IConverterRegister {


    private static final Logger LOGGER = Logger.getLogger(CsvBeanObjectReader.class);


    private final List<Object> processedColumns = new ArrayList<>();
    private final MethodCache cache = new MethodCache();
    private IConverterHolder converterHolder;


    public CsvBeanObjectReader(final Reader reader, final CsvPreference preferences) {
        super(reader, preferences);
        converterHolder = new DefaultConverterHolder();
    }


    @Override
    public <T> T read(final Class<T> cls, FieldDto[] filedDtos) throws Exception {
        if (readRow()) {
            if (filedDtos.length != length()) {
                throw new IllegalArgumentException(String.format(
                        "the nameMapping array and the number of columns read "
                                + "should be the same size (nameMapping length = %d, columns = %d)", filedDtos.length,
                        length()));
            }
            processedColumns.clear();
            processedColumns.addAll(getColumns());
            return populateBean(instantiateBean(cls), filedDtos);
        }
        return null;
    }


    private static <T> T instantiateBean(final Class<T> clazz) {
        final T bean;
        if (clazz.isInterface()) {
            bean = BeanInterfaceProxy.createProxy(clazz);
        } else {
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new SuperCsvReflectionException(String.format(
                        "error instantiating bean, check that %s has a default no-args constructor", clazz.getName()), e);
            } catch (IllegalAccessException e) {
                throw new SuperCsvReflectionException("error instantiating bean", e);
            }
        }

        return bean;
    }


    private static void invokeSetter(final Object bean, final Method setMethod, final Object fieldValue) {
        try {
            setMethod.invoke(bean, fieldValue);
        } catch (final Exception e) {
            throw new SuperCsvReflectionException(String.format("error invoking method %s()", setMethod.getName()), e);
        }
    }


    private <T> T populateBean(final T resultBean, final FieldDto[] filedDtos) throws Exception {
        Converter converter = null;
        Object newValue = null;
        for (int i = 0; i < filedDtos.length; i++) {

            final Object fieldValue = processedColumns.get(i);
            if (filedDtos[i] == null || fieldValue == null) {
                continue;
            }
            try {
                Field filed = resultBean.getClass().getDeclaredField(filedDtos[i].getFieldName());
                Method setMethod = cache.getSetMethod(resultBean, filedDtos[i].getFieldName(), filed.getType());
                converter = this.converterHolder.getConverter(filed.getType().getTypeName());
                if (converter != null) {
                    newValue = converter.stringToObject(String.valueOf(fieldValue));
                    invokeSetter(resultBean, setMethod, newValue);
                } else {
                    invokeSetter(resultBean, setMethod, fieldValue);
                }
            } catch (Exception e) {
                throw new Exception("field :" + filedDtos[i].getFieldName() + "not  found", e);
            }
        }
        return resultBean;
    }


    /**
     * 注册类型转换器
     *
     * @param converter
     */
    @Override
    public void registerConverter(Converter converter) {
        if (Objects.isNull(converterHolder)) {
            throw new NullPointerException("converterHolder should not be null");
        }
        this.converterHolder.addConverter(converter);
    }
}
