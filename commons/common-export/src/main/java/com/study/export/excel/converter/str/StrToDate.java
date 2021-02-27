package com.study.export.excel.converter.str;

import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.LocaleUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StrToDate implements StrToObjectConverter {


    private final Calendar EXCEL_EPOCH_CAL =
            LocaleUtil.getLocaleCalendar(1904, 0, 1);

    @Override
    public Object toTargetValue(FieldDto fieldConfig, String obj) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        String dateFormater = fieldConfig.getDataFormat();
        if (obj.matches("\\d+\\.?\\d+")) {
            String strLongValue = obj.split("\\.")[1];
            return DateUtil.getJavaDate(Double.valueOf(strLongValue));
        }
        if (StringUtils.isNotBlank(dateFormater)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(fieldConfig.getDataFormat());
                return dateFormat.parse(obj);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}
