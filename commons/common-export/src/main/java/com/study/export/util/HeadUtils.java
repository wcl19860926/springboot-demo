package com.study.export.util;

import com.study.export.annotation.ExportField;
import com.study.export.annotation.IEnum;
import com.study.export.dto.FieldDto;
import com.study.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public final class HeadUtils {

    /**
     * 只获取有@ExportField  annotation 标记的字段
     * 并获取该字段的get  ,set， converter
     *
     * @param cls
     * @return
     */
    public static List<FieldDto> getExportFiledDto(Class<?> cls) throws Exception {
        List<FieldDto> fieldDtoList = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        String title, dateFormat, keyValue;
        int order;
        for (Field field : fields) {
            ExportField exportAnnotation = field.getAnnotation(ExportField.class);
            if (exportAnnotation != null) {
                title = exportAnnotation.title();
                if (StringUtils.isBlank(title)) {
                    title = field.getName();
                }
                title = title.trim();
                dateFormat = exportAnnotation.dataFormat();
                order = exportAnnotation.cols();
                FieldDto dto = HeadUtils.getFieldDto(cls, field);
                dto.setValueMap(HeadUtils.getKeyValueMap(exportAnnotation.enumValue()));
                dto.setType(field.getType());
                dto.setOrder(order);
                dto.setDataFormat(dateFormat);
                dto.setFieldName(field.getName());
                dto.setFiledTitle(title);
                fieldDtoList.add(dto);
            }
        }
        return fieldDtoList;
    }


    private static Map<String, String> getKeyValueMap(Class<? extends IEnum> enumClass) throws Exception {
        Map<String, String> keyValueMap = new HashMap<>();
        if (enumClass != null && enumClass != IEnum.class) {
            Field[] enumFields = enumClass.getFields();
            if (enumFields != null && enumFields.length > 0) {
                for (Field f : enumFields) {
                    IEnum obj = (IEnum) f.get(enumClass);
                    keyValueMap.put(obj.getCode(), obj.getName());
                }
            }
        }
        return keyValueMap;
    }


    private static FieldDto getFieldDto(Class<?> cls, Field field) throws Exception {
        FieldDto dto = new FieldDto();
        String fieldName = field.getName();
        Method getMethod = ClassUtils.getGetMethodByFieldName(cls, fieldName);
        if (getMethod == null) {
            final String booleanGetterName = ClassUtils.getMethodNameForField(ClassUtils.IS_PREFIX, fieldName);
            getMethod = ClassUtils.getMethodByMethodName(cls, booleanGetterName);
        }
        if (getMethod == null) {
            getMethod = ClassUtils.getMethodByMethodName(cls, fieldName);
        }
        if (getMethod == null) {
            throw new Exception("the  field " + fieldName + "  not  have a getMethod!");
        }
        Method setMethod = ClassUtils.getSetMethodByFieldName(cls, fieldName, field.getType());
        if (setMethod == null) {
            setMethod = ClassUtils.getMethodByMethodNameAndParametrTypes(cls, ClassUtils.SET_PREFIX + "" + fieldName.substring(2, fieldName.length()), field.getType());
        }
        if (setMethod == null) {
            throw new Exception("the  field " + fieldName + "  not  have a setMethod!");
        }
        dto.setGetMethod(getMethod);
        dto.setSetMethod(setMethod);

        return dto;
    }


    /**
     * @param cls 获出需要导出实体类字段，及字段title
     * @return
     * @throws Exception
     */
    public static FieldDto[] getExplortHeadFieldDtos(Class<?> cls) throws Exception {
        List<FieldDto> fieldDtoList = HeadUtils.getExportFiledDto(cls);
        if (CollectionUtils.isEmpty(fieldDtoList)) {
            throw new Exception("there  is  no  filed to export !");
        }
        //判断order是否有相同，若有相同，抛出异常
        List<Integer> orderList = new ArrayList<>();
        for (FieldDto dto : fieldDtoList) {
            if (dto.getOrder() > 0) {
                if (orderList.contains(dto.getOrder())) {
                    throw new Exception("Repeated export cols flags  on field [" + dto.getFieldName() + "]");
                } else {
                    orderList.add(dto.getOrder());
                }
            }
        }
        //排序，找出order最大值
        Collections.sort(orderList);
        int size = orderList.size();
        int max = orderList.get(size - 1);
        if (max > 1000) {
            throw new Exception("the  order value [" + max + " ] is  big  max  value   [" + 1000 + "]");
        }
        int min = orderList.get(0);
        if (min < 1) {
            throw new Exception("the  order value start with 1  ");
        }
        FieldDto[] head = new FieldDto[max];
        //优先排序cols>0
        for (FieldDto dto : fieldDtoList) {
            if (dto.getOrder() > 0) {
                head[dto.getOrder() - 1] = dto;
            }
        }
        //再将未设置 cols=0 的 ，放入head未被占用的数组元系中
        int index = 0;
        for (FieldDto dto : fieldDtoList) {
            if (dto.getOrder() <= 0) {
                boolean setSuccess = false;
                int i = index;
                while (!setSuccess && i < max) {
                    if (head[i] == null) {
                        head[i] = dto;
                        index++;
                        head[i].setOrder(i + 1);
                        setSuccess = true;
                    }
                    i++;
                }
            }
        }
        return head;
    }


    /**
     * @param headDtos 获出需要导出字段title数组
     * @return
     * @throws Exception
     */
    public static String[] getHeadFieldTitles(FieldDto[] headDtos) {
        String[] headTitles = new String[headDtos.length];
        int i = 0;
        for (FieldDto dto : headDtos) {
            if (dto == null) {
                headTitles[i] = " ";
            } else {
                headTitles[i] = dto.getFiledTitle();
            }
            i++;
        }
        return headTitles;
    }

    /**
     * 根据读出的headValue设置FieldDto字段的读取顺序
     *
     * @param cls
     * @param headValues
     * @return
     * @throws Exception
     */
    public static FieldDto[] getImportHeadFieldDtos(Class<?> cls, String[] headValues) throws Exception {
        List<FieldDto> fieldDtoList = HeadUtils.getExportFiledDto(cls);
        if (CollectionUtils.isEmpty(fieldDtoList)) {
            return new FieldDto[0];
        }
        if (headValues != null && headValues.length != 0) {
            int len = headValues.length;
            FieldDto[] head = new FieldDto[len];
            String title;
            for (int i = 0; i < len; i++) {
                title = headValues[i];
                if (StringUtils.isNotBlank(title)) {
                    title = title.trim();
                    for (FieldDto dto : fieldDtoList) {
                        if (title.equalsIgnoreCase(dto.getFiledTitle())) {
                            dto.setOrder(i + 1);
                            head[i] = dto;
                        }
                    }
                }
            }
            return head;
        }
        return new FieldDto[0];
    }

    /**
     * @param cls 获出需要导出实体类字段，及字段title
     * @return
     * @throws Exception
     */
    public static FieldDto[] getExcelImportHeadFieldDtos(Class<?> cls, Row headRow) throws Exception {
        List<String> headTitleList = new ArrayList<>();
        if (headRow != null) {
            int i = 0;
            Cell cell;
            String value;
            while (true) {
                cell = headRow.getCell(i);
                if (cell == null) {
                    break;
                }
                value = cell.getStringCellValue();
                if ("".equals(value)) {
                    value = "";
                }
                headTitleList.add(value);
                i++;
            }
        }
        return getImportHeadFieldDtos(cls, headTitleList.toArray(new String[headTitleList.size()]));
    }
}
