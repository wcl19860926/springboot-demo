package com.study.util;


import com.study.export.dto.FieldDto;
import com.study.export.excel.converter.Converter;
import com.study.export.excel.exception.ExportException;
import com.study.export.excel.util.ExcelUtil;
import com.study.export.util.ClassUtils;
import com.study.export.util.HeadUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;


public final class ExcelHelperUtil {


    /**
     * 构造方法
     */
    private ExcelHelperUtil() {

    }

    private static final Logger LOGGER = Logger.getLogger(ExcelHelperUtil.class);

    /**
     * 默认的时间格试
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";// 默认日期格式

    /**
     * 默认宽度
     */
    public static final int DEFAULT_COLUMN_WIDTH = 17;

    /**
     * 每个sheet页写入的最大行数
     */

    public static final int DEFALUT_MAX_SHEET_RECORD = 65535;


    /**
     * 将数据写入到excel中
     *
     * @param workbook
     * @param objList
     * @param datePattern
     * @param tableTitle
     * @param <T>
     * @throws Exception
     */
    public static <T> void exportExcel(Workbook workbook, List<T> objList, String datePattern,
                                       String tableTitle) throws Exception {
        if (CollectionUtils.isEmpty(objList)) {
            return;
        }
        FieldDto[] fieldDtos = HeadUtils.getExplortHeadFieldDtos(objList.get(0).getClass());
        if (datePattern == null) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        // 表头样式
        CellStyle titleStyle = ExcelUtil.createHeaderStyle(workbook);
        // 列头样式
        CellStyle headerStyle = ExcelUtil.createTitleStyle(workbook);
        CellStyle cellStyle = ExcelUtil.createCommonCellStyle(workbook);
        Map<String, CellStyle> dateFormatMap = setDateFormatId2Dto(workbook, fieldDtos);
        // 设置列宽
        int minBytes = DEFAULT_COLUMN_WIDTH;// 至少字节数
        int index = 0;
        int count = 0;
        Row row;
        Sheet sheet = null;
        if (CollectionUtils.isNotEmpty(objList)) {
            for (Object obj : objList) {
                if (count % DEFALUT_MAX_SHEET_RECORD == 0) {// 每个表单最大写入60000行
                    sheet = workbook.createSheet();
                    ExcelHelperUtil.setHeader(fieldDtos, titleStyle, headerStyle, minBytes, sheet, tableTitle);
                    index = 2;
                }
                row = sheet.createRow(index);
                ExcelHelperUtil.writeObjectToRow(row, fieldDtos, obj, 0, cellStyle, dateFormatMap);
                index++;
                count++;
            }
        } else {
            sheet = workbook.createSheet();
            ExcelHelperUtil.setHeader(fieldDtos, titleStyle, headerStyle, minBytes, sheet, tableTitle);
        }

    }


    /**
     * 将取出的单元格的值赋给特定的Object
     *
     * @param obj
     * @param cell
     * @param dto
     * @throws Exception
     */
    public static void setObjectFieldValue(Object obj, Cell cell, FieldDto dto) throws Exception {
        if (cell == null || dto == null) {
            return;
        }
        Object cellValue = ExcelUtil.getCellValue(cell);
        if (cellValue != null) {
            Method setMethod = dto.getSetMethod();
            Converter converter = ExcelUtil.getConverter(dto.getType().getTypeName());
            try {
                if (converter != null) {
                    setMethod.invoke(obj, converter.toTargetValue(dto, cellValue));
                } else {
                    setMethod.invoke(obj, cellValue);
                }
            } catch (Exception e) {
                throw new ExportException("set  value  " + cellValue + " for  field " + dto.getFieldName() + "fialed ! ", e);
            }
        }
    }


    /**
     * 从Sheet中读取数据并返回对应javaBean的List
     *
     * @param sheet
     * @param fieldDtos 表头
     * @param startRow
     * @param lastRow
     * @return
     * @throws Exception
     */
    public static <T> List<T> readData(Sheet sheet, Class<T> cls, FieldDto[] fieldDtos, int startRow, int lastRow) throws Exception {
        List<T> objList = new ArrayList<>();
        Row row;
        for (int i = startRow; i <= lastRow; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                T obj = ExcelHelperUtil.readOneRow(row, cls, fieldDtos, 0);
                objList.add(obj);
            }
        }
        return objList;
    }


    /**
     * @param sheet
     * @param cls
     * @param startRow
     * @param lastRow
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> readData(Sheet sheet, Class<T> cls, int startRow, int lastRow) throws Exception {
        if (lastRow - startRow <= 0) {
            return new ArrayList<>();
        }
        FieldDto[] fieldDtos = HeadUtils.getExcelImportHeadFieldDtos(cls, sheet.getRow(1));
        return ExcelHelperUtil.readData(sheet, cls, fieldDtos, startRow, lastRow);
    }


    /**
     * @param file excel2007  文件
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> readData(File file, Class<T> cls) throws Exception {
        Workbook workbook = ExcelUtil.createXssfWorkbook(file);
        return readDate(workbook, cls);
    }


    /**
     * excel2007 文件输入流
     *
     * @param inputStream
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> readData(InputStream inputStream, Class<T> cls) throws Exception {
        Workbook workbook = ExcelUtil.createXssfWorkbook(inputStream);
        return readDate(workbook, cls);
    }

    /**
     * 默认读取第一个sheet  的第三行开始读取
     * excel2007 Workbook
     *
     * @param workbook
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> readDate(Workbook workbook, Class<T> cls) throws Exception {
        int startRow = 2;
        int i = startRow;
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(i);
        Cell cell = row.getCell(0);
        while (!Objects.isNull(row) && !Objects.isNull(cell) && ObjectUtils.isNotEmpty(ExcelUtil.getCellValue(cell))) {
            i++;
            row = sheet.getRow(i);
            if (!Objects.isNull(row)) {
                cell = row.getCell(0);
            }
        }
        return readData(sheet, cls, startRow, i - 1);
    }


    /**
     * 从row 中读取一行，并反回一个javaBean对象
     *
     * @param row
     * @param fieldDtos
     * @param startIndex
     * @return
     * @throws Exception
     */
    private static <T> T readOneRow(Row row, Class<T> cls, FieldDto[] fieldDtos, int startIndex) throws Exception {
        int i = startIndex;
        Cell cell;
        T obj = ClassUtils.getInstanceByClass(cls);
        for (FieldDto dto : fieldDtos) {
            if (!Objects.isNull(dto)) {
                //order是从1开始，Excel下标是从零开始，所以要减1
                i = startIndex + dto.getOrder() - 1;
                cell = row.getCell(i);
                ExcelHelperUtil.setObjectFieldValue(obj, cell, dto);
            }
        }
        return obj;
    }

    /**
     * 将对象的值根据header 写入一行
     */

    private static void writeObjectToRow(Row row, FieldDto[] fieldDtos, Object o, int startIndex,
                                         CellStyle cellStyle, Map<String, CellStyle> dateFormatMap) {
        Object value = null;
        Method method;
        int i = 0;
        int j;
        for (FieldDto dto : fieldDtos) {
            if (!Objects.isNull(dto)) {
                j = startIndex + dto.getOrder() - 1;
                try {
                    method = dto.getGetMethod();
                    value = method.invoke(o);
                    CellStyle dateCellStyle = dateFormatMap.get(dto.getDataFormat());
                    if (Objects.isNull(dateCellStyle)) {
                        dateCellStyle = dateFormatMap.get(DEFAULT_DATE_PATTERN);
                    }
                    ExcelUtil.writeDataToCell(row.createCell(j), value, dto, dateCellStyle, cellStyle);
                } catch (Exception e) {
                    LOGGER.error("invoke  getMethod  and  write  value:[" + value + "] to   cel  failed !", e);
                }
            } else {
                j = i + startIndex;
                ExcelUtil.writeDataToCell(row.createCell(j), "", null, cellStyle, cellStyle);
            }
            i++;
        }
    }


    /**
     * 获取所有的时间格式， 并创建对应dateFormatStyle
     *
     * @param workbook
     * @param fieldDtos
     */
    private static Map<String, CellStyle> setDateFormatId2Dto(Workbook workbook, FieldDto[] fieldDtos) {
        Set<String> dateFormatSet = new LinkedHashSet<>();
        dateFormatSet.add(DEFAULT_DATE_PATTERN);// 增加一个默认的时间处理格式
        for (FieldDto dto : fieldDtos) {
            if (!Objects.isNull(dto) && StringUtils.isNotBlank(dto.getDataFormat())) {
                dateFormatSet.add(dto.getDataFormat());
            }
        }
        Map<String, CellStyle> formatMap = new LinkedHashMap<>();
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle;
        for (String str : dateFormatSet) {
            dateCellStyle = ExcelUtil.createCommonCellStyle(workbook);
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(str));
            formatMap.put(str, dateCellStyle);
        }
        return formatMap;
    }

    /**
     * 设置表头
     *
     * @param fieldDtos   表头行
     * @param titleStyle
     * @param headerStyle
     * @param minBytes
     * @param sheet
     */
    private static void setHeader(FieldDto[] fieldDtos, CellStyle titleStyle, CellStyle headerStyle, int minBytes,
                                  Sheet sheet, String tableTitle) {
        //导出报表的标题
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(titleStyle);
        titleCell.setCellValue(tableTitle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fieldDtos.length - 1));
        Row headerRow = sheet.createRow(1);
        int columnWidth = 0;
        int bytes = 0;//设置表头行
        String[] headTitles = HeadUtils.getHeadFieldTitles(fieldDtos);
        int i = 0;
        String dataFormat;
        for (String title : headTitles) {
            if (StringUtils.isNotBlank(title)) {
                dataFormat = fieldDtos[i].getDataFormat();
                if (StringUtils.isNotBlank(dataFormat)) {
                    bytes = dataFormat.getBytes().length + 8;
                } else {
                    bytes = title.getBytes().length;
                }
                columnWidth = bytes < minBytes ? minBytes : bytes;
                sheet.setColumnWidth(i, columnWidth * 256);
            }
            headerRow.createCell(i).setCellValue(title);
            headerRow.getCell(i).setCellStyle(headerStyle);
            i++;
        }
    }


}

