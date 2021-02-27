package com.study.export.excel.util;


import com.study.export.dto.FieldDto;
import com.study.export.excel.converter.*;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ExcelUtil {


    /**
     * 默认的转换器
     */
    private static Map<String, Converter> defaultConverterMap;

    static {
        defaultConverterMap = new HashMap<>();
        defaultConverterMap.put(Long.class.getTypeName(), new DoubleToLong());
        defaultConverterMap.put(long.class.getTypeName(), new DoubleToBaseLong());
        defaultConverterMap.put(Integer.class.getTypeName(), new DoubleToInteger());
        defaultConverterMap.put(int.class.getTypeName(), new DoubleToInt());
        defaultConverterMap.put(boolean.class.getTypeName(), new ObjectToBaseBoolean());
        defaultConverterMap.put(Float.class.getTypeName(), new DoubleToFloat());
        defaultConverterMap.put(float.class.getTypeName(), new DoubleToBaseFloat());
        defaultConverterMap.put(double.class.getTypeName(), new DoubleToBaseDouble());
        defaultConverterMap.put(BigDecimal.class.getTypeName(), new DoubleToBigDecimal());
        defaultConverterMap.put(Date.class.getTypeName(), new ObjectToDate());
        defaultConverterMap.put(String.class.getTypeName(), new StringToString());
        defaultConverterMap.put(Boolean.class.getTypeName(), new ObjectToBoolean());


    }


    public static Converter getConverter(String typeName) {
        return defaultConverterMap.get(typeName);
    }


    /**
     * @return
     * @throws IOException
     */
    public static Workbook createXssfWorkbook() throws IOException {
        return new XSSFWorkbook();
    }


    /**
     * @return
     * @throws IOException
     */
    public static Workbook createXssfWorkbook(File file) throws Exception {
        return new XSSFWorkbook(file);
    }


    /**
     * @return
     * @throws IOException
     */
    public static Workbook createXssfWorkbook(InputStream inputStream) throws Exception {
        return new XSSFWorkbook(inputStream);
    }


    /**
     * @return
     * @throws IOException
     */
    public static Workbook createHssfWorkbook() throws Exception {
        return new HSSFWorkbook();
    }


    /**
     * 设置 单元格边框为红色
     *
     * @param cellStyle
     * @return
     */
    public static void setRedBorderColorStyle(CellStyle cellStyle) {
        setBorderColorStyle(cellStyle, HSSFColor.HSSFColorPredefined.RED.getIndex());
    }


    /**
     * 设置 单元格边框为红色
     *
     * @param cellStyle
     * @return
     */
    public static void setBorderColorStyle(CellStyle cellStyle, short color) {
        cellStyle.setBottomBorderColor(color);
        cellStyle.setTopBorderColor(color);
        cellStyle.setLeftBorderColor(color);
        cellStyle.setRightBorderColor(color);

    }


    /**
     * 设置 单元格边框
     *
     * @param cellStyle
     * @return
     */
    public static void setBorderThinStyle(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.THIN); // 下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);// 左边框
        cellStyle.setBorderTop(BorderStyle.THIN);// 上边框
        cellStyle.setBorderRight(BorderStyle.THIN);// 右边框

    }

    /**
     * 设置 单元格边框
     *
     * @param cellStyle
     * @return
     */
    public static void setAlignCenter(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中

    }

    /**
     * @param cellStyle
     * @param Font
     * @param fontSize
     * @param fontName
     * @return
     */

    public static void setFont(CellStyle cellStyle, Font Font, short fontSize, String fontName) {
        Font.setFontHeightInPoints(fontSize); // 字体高度
        Font.setFontName(fontName); // 字体样式
        cellStyle.setFont(Font);

    }

    /**
     * @param cellStyle
     * @param font
     * @param fontSize
     * @param fontName
     */

    public static void setTitleFont(CellStyle cellStyle, Font font, short fontSize, String fontName) {
        setFont(cellStyle, font, fontSize, fontName);
        font.setBold(true);
    }


    /**
     * 设置 表头样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        ExcelUtil.setBorderThinStyle(titleStyle);
        ExcelUtil.setAlignCenter(titleStyle);
        Font titleFont = workbook.createFont();
        ExcelUtil.setTitleFont(titleStyle, titleFont, (short) 20, "黑体");
        return titleStyle;
    }

    /**
     * 设置 表头样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        ExcelUtil.setBorderThinStyle(titleStyle);
        ExcelUtil.setAlignCenter(titleStyle);
        Font titleFont = workbook.createFont();
        ExcelUtil.setTitleFont(titleStyle, titleFont, (short) 15, "黑体");
        return titleStyle;
    }

    /**
     * 设置 表头样式
     *
     * @param workbook
     * @return
     */
    public static CellStyle createCommonCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        ExcelUtil.setBorderThinStyle(cellStyle);
        ExcelUtil.setAlignCenter(cellStyle);
        Font titleFont = workbook.createFont();
        ExcelUtil.setFont(cellStyle, titleFont, (short) 15, "黑体");
        return cellStyle;
    }


    /**
     * 创建有指定时间格试的单元格
     *
     * @param workbook
     * @param dateFromat
     * @return
     */
    public static CellStyle createDateCellStyle(Workbook workbook, String dateFromat) {
        CellStyle dateCellStyle = ExcelUtil.createCommonCellStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFromat));
        return dateCellStyle;
    }


    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    } else {
                        return cell.getNumericCellValue();
                    }
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "";
            }
        }
        return null;
    }

    /**
     * 将一个数据写入到单元格
     *
     * @param cell
     * @param value
     * @param dataFormatStyle
     */
    public static void writeDataToCell(Cell cell, Object value, FieldDto dto, CellStyle dataFormatStyle, CellStyle cellStyle) {
        if (cell == null) {
            return;
        }
        if (value == null) {
            return;
        }
        if (Objects.isNull(dto)) {
            cell.setCellValue(String.valueOf(value));
            cell.setCellStyle(cellStyle);
            return;
        }
        if (value instanceof Date) {
            cell.setCellStyle(dataFormatStyle);
            cell.setCellValue((Date) value);
        } else if (value instanceof Number) {
            if (value instanceof Integer || value instanceof Short) {
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                cell.setCellStyle(cellStyle);
                cell.setCellValue(Double.valueOf(String.valueOf(value)));
            } else if (value instanceof Float || value instanceof Double) {
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(" 0.00"));
                cell.setCellStyle(cellStyle);
                if (value instanceof Float) {
                    cell.setCellValue(Double.valueOf(String.valueOf(value)));
                } else {
                    cell.setCellValue((Double) value);
                }
            } else if (value instanceof BigDecimal) {//BigDecimal默认表示为金额，
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                cell.setCellStyle(cellStyle);
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else {
                cell.setCellStyle(cellStyle);
                cell.setCellValue(Double.valueOf(String.valueOf(value)));
            }
        } else if (value instanceof Boolean) {
            if (dto.getValueMap() != null && !dto.getValueMap().isEmpty()) {
                cell.setCellValue(dto.getValueMap().get(String.valueOf(value)));
                cell.setCellStyle(cellStyle);
            } else {
                cell.setCellValue((Boolean) value);
                cell.setCellStyle(cellStyle);
            }
        } else {
            if (dto.getValueMap() != null && !dto.getValueMap().isEmpty()) {
                cell.setCellValue(dto.getValueMap().get(String.valueOf(value)));
                cell.setCellStyle(cellStyle);
            } else {
                cell.setCellValue(String.valueOf(value));
                cell.setCellStyle(cellStyle);
            }
        }
    }


}
