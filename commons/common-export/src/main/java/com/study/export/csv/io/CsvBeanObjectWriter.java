package com.study.export.csv.io;


import com.study.export.csv.converter.Converter;
import com.study.export.csv.converter.DefaultConverterHolder;
import com.study.export.csv.converter.IConverterHolder;
import com.study.export.csv.converter.IConverterRegister;
import com.study.export.dto.FieldDto;
import org.apache.log4j.Logger;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.MethodCache;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvBeanObjectWriter extends AbstractCsvWriter implements ICsvBeanObjectWriter, IConverterRegister {


    private static final Logger LOGGER = Logger.getLogger(CsvBeanObjectWriter.class);

    private IConverterHolder converterHolder;


    private final List<Object> beanValues = new ArrayList<Object>();


    private final MethodCache cache = new MethodCache();


    public CsvBeanObjectWriter(final Writer writer, final CsvPreference preference) {
        super(writer, preference);
        converterHolder = new DefaultConverterHolder();

    }


    /**
     * {@inheritDoc}
     *
     * @param source
     * @param heads  FieldDto[]
     */
    @Override
    public void write(Object source, FieldDto[] heads) throws IOException {
        super.incrementRowAndLineNo();
        extractBeanValues(source, heads);
        super.writeRow(beanValues);
    }


    private void extractBeanValues(final Object source, final FieldDto[] heads) {

        if (source == null) {
            throw new NullPointerException("the bean to write should not be null");
        } else if (heads == null) {
            throw new NullPointerException(
                    "the nameMapping array can't be null as it's used to map from fields to columns");
        }
        beanValues.clear();
        Object value = null;
        String converValue = null;
        Converter converter = null;
        int length = heads.length;
        for (int i = 0; i < length; i++) {
            final FieldDto headDto = heads[i];
            if (headDto == null) {
                beanValues.add(null);
            } else {
                Method getMethod = cache.getGetMethod(source, headDto.getFieldName());
                try {
                    value = getMethod.invoke(source);
                    if (value != null) {
                        converter = converterHolder.getConverter(value.getClass().getTypeName());
                        if (converter != null) {
                            converValue = converter.objectToString(value);
                            beanValues.add(converValue);
                        } else {
                            beanValues.add(value);
                        }
                    } else {
                        beanValues.add(value);
                    }
                } catch (final Exception e) {
                    throw new SuperCsvReflectionException(String.format("error extracting bean value for field %s",
                            headDto.getFieldName()), e);
                }
            }
        }
    }


    /**
     * 注册类型转换器
     *
     * @param converter
     */
    @Override
    public void registerConverter(Converter converter) {
        if (Objects.isNull(converter)) {
            throw new NullPointerException("converter should not be null");
        }
        this.converterHolder.addConverter(converter);
    }


    /**
     * Closes the underlying writer, flushing it first.
     */
    @Override
    public void close() throws IOException {
        super.close();
    }
}
