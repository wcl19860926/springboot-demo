package com.study.export.excel.converter;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ObjectToDate implements Converter<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectToDate.class);

    @Override
    public Date toTargetValue(FieldDto fieldConfig, Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (StringUtils.isNotBlank(fieldConfig.getDataFormat())) {
            if (value instanceof String) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(fieldConfig.getDataFormat());
                    return dateFormat.parse((String) value);
                } catch (ParseException e) {
                    LOGGER.error("process  data field " + fieldConfig.getFieldName() + "faild!");
                    return null;
                }
            }
        }
        return null;
    }
}
