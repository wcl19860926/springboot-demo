package com.study.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 050355
 *
 */
public final class ExcelUtils {

	/**
	 * 构造方法
	 */
	private ExcelUtils() {

	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);
	/**
	 * 2007板Excel
	 */
	public static final String EXCEL_FILE_EXTENTION_XLSX = "xlsx";

	/**
	 * 2003 --- 2007板Excel
	 */
	public static final String EXCEL_FILE_EXTENTION_XLS = "xls";

	/**
	 * 默认的时间格试
	 */
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";// 默认日期格式

	/**
	 * 默认宽度
	 */
	public static final int DEFAULT_COLOUMN_WIDTH = 17;

	/**
	 * 每个sheet页写入的最大行数
	 */

	public static final int DEFALUT_MAX_SHEET_RECORD = 65535;

	/**
	 * 创建Excel 工作薄 如果给定文件名为空， 则返回null;
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Workbook createWorkbook(InputStream input, String fileSurefix) throws IOException {
		Workbook workbook = null;
		if (input != null) {
			if (EXCEL_FILE_EXTENTION_XLSX.equals(fileSurefix)) {
				workbook = new XSSFWorkbook(input);
			} else if (EXCEL_FILE_EXTENTION_XLS.equals(fileSurefix)) {
				workbook = new HSSFWorkbook(input);
			} else {
				workbook = new XSSFWorkbook(input);
			}
		}
		return workbook;

	}

	/**
	 * 创建单元格样式
	 * 
	 * @param wb
	 * @return
	 */
	public static CellStyle createCellStyle(Workbook wb) {
		return createCommonCellStyle(wb);
	}

	/**
	 * 
	 * 设置 单元格边框为红色
	 * 
	 * @param cellStyle
	 * @return
	 */
	public static void setRedBorderColorStyle(CellStyle cellStyle) {
		setBorderColorStyle(cellStyle, HSSFColor.RED.index);
	}

	/**
	 * 
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
	 * 
	 * 设置 单元格边框为红色
	 * 
	 * @param cellStyle
	 * @return
	 */
	public static void setBlackBorderColorStyle(CellStyle cellStyle) {
		setBorderColorStyle(cellStyle, HSSFColor.BLACK.index);
	}

	/**
	 * 
	 * 设置 单元格边框
	 * 
	 * @param cellStyle
	 * @return
	 */
	public static void setBorderThinStyle(CellStyle cellStyle) {
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

	}

	/**
	 * 
	 * 设置 单元格边框
	 * 
	 * @param cellStyle
	 * @return
	 */
	public static void setAlignCenter(CellStyle cellStyle) {
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中

	}

	/**
	 * 
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
	 * 
	 * @param cellStyle
	 * @param Font
	 * @param fontSize
	 * @param fontName
	 * @return
	 */

	public static void setTitileFont(CellStyle cellStyle, Font font, short fontSize, String fontName) {
		setFont(cellStyle, font, fontSize, fontName);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	}

	/**
	 * 读取一行数据
	 * 
	 * @param sheet
	 * @param row
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Object> readOneRowData(Row row, int start, int end) {
		List<Object> rowDataList = new ArrayList<Object>(end - start + 1);
		for (int j = start; j < end; j++) {
			rowDataList.add(ExcelUtils.getCellValue(row.getCell(j)));
		}
		return rowDataList;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		Object value = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				String cellValue = null;
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					cellValue = sFormat.format(date);
				} else {
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);// true时的格式：1,234,567,890
					nf.setMaximumFractionDigits(2); // 设置最多小数位为4位
					double acno = cell.getNumericCellValue();
					cellValue = nf.format(acno);
				}
				value = cellValue;
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				value = "";
				break;
			}
			return value;
		}
		return null;
	}

	/**
	 * 将一个数据写入到单元格
	 * 
	 * @param cell
	 * @param value
	 * @param dataFormat
	 */
	public static void writeDataToCell(Cell cell, Object value, String dataFormat, CellStyle cellStyle) {
		if (cell == null) {
			return;
		}
		if (dataFormat == null) {
			dataFormat = ExcelUtils.DEFAULT_DATE_PATTERN;
		}
		String cellValue = null;
		if (value == null) {
			cellValue = "";
		} else if (value instanceof Date) {
			cellValue = new SimpleDateFormat(dataFormat).format(value);
		} else if (value instanceof Float || value instanceof Double) {
			cellValue = new BigDecimal(value.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		} else {
			cellValue = value.toString();
		}
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}

	public static void writeObjectToRow(Row row, Set<String> header, Object o, int startIndex, CellStyle cellStyle) {
		ExcelUtils.writeObjectToRow(row, header, o, o.getClass(), startIndex, cellStyle);

	}

	/**
	 * 将对象的值根据header 写入一行
	 */

	private static void writeObjectToRow(Row row, Set<String> header, Object o, Class<?> cls, int startIndex,
			CellStyle cellStyle) {
		Object value = null;
		Method method = null;
		int i = startIndex;
		for (String key : header) {
			try {
				method = cls.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length()));
				value = method.invoke(o);
				ExcelUtils.writeDataToCell(row.createCell(i), value, null, cellStyle);
			} catch (Exception e) {
				LOGGER.error("write value  to cell , \t value:[" + value + "] failed !", e);
			}
			i++;
		}
	}

	/**
	 * 设置 表头样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle titleStyle = workbook.createCellStyle();
		ExcelUtils.setBorderThinStyle(titleStyle);
		ExcelUtils.setAlignCenter(titleStyle);
		Font titleFont = workbook.createFont();
		ExcelUtils.setTitileFont(titleStyle, titleFont, (short) 20, "黑体");
		return titleStyle;
	}

	/**
	 * 设置 表头样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle createTitileStyle(Workbook workbook) {
		CellStyle titleStyle = workbook.createCellStyle();
		ExcelUtils.setBorderThinStyle(titleStyle);
		ExcelUtils.setAlignCenter(titleStyle);
		Font titleFont = workbook.createFont();
		ExcelUtils.setTitileFont(titleStyle, titleFont, (short) 15, "黑体");
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
		ExcelUtils.setBorderThinStyle(cellStyle);
		ExcelUtils.setAlignCenter(cellStyle);
		Font titleFont = workbook.createFont();
		ExcelUtils.setFont(cellStyle, titleFont, (short) 15, "黑体");
		return cellStyle;
	}

	/**
	 * 将数据写入到excel中
	 * 
	 * @param title
	 * @param headMap
	 * @param obj
	 * @param datePattern
	 * @param colWidth
	 * @param workbook
	 */
	public static void exportExcelX(Map<String, String> headMap, List<?> objList, String datePattern, int colWidth,
			Workbook workbook, String titile) {
		if (datePattern == null) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		if (objList == null) {
			objList = new ArrayList<Object>();
		}
		// 表头样式
		CellStyle titleStyle = ExcelUtils.createHeaderStyle(workbook);
		// 列头样式
		CellStyle headerStyle = ExcelUtils.createTitileStyle(workbook);

		CellStyle cellStyle = ExcelUtils.createCommonCellStyle(workbook);
		// 设置列宽
		int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
		// 表头的key
		Set<String> headerKey = headMap.keySet();
		int index = 0;
		int count = 0;
		Row row = null;
		SXSSFSheet sheet = null;
		if (CollectionUtils.isNotEmpty(objList)) {
			for (Object obj : objList) {
				if (count % DEFALUT_MAX_SHEET_RECORD == 0) {// 每个表单最大写入60000行
					sheet = (SXSSFSheet) workbook.createSheet();
					ExcelUtils.setHeader(headMap, titleStyle, headerStyle, minBytes, sheet, titile);
					index = 2;
				}
				row = sheet.createRow(index);
				ExcelUtils.writeObjectToRow(row, headerKey, obj, 0, cellStyle);
				index++;
				count++;
			}
		} else {
			sheet = (SXSSFSheet) workbook.createSheet();
			ExcelUtils.setHeader(headMap, titleStyle, headerStyle, minBytes, sheet, titile);
		}

	}

	/**
	 * 设置表头
	 * 
	 * @param headMap
	 * @param titleStyle
	 * @param headerStyle
	 * @param minBytes
	 * @param sheet
	 */
	public static void setHeader(Map<String, String> headMap, CellStyle titleStyle, CellStyle headerStyle, int minBytes,
			SXSSFSheet sheet, String titile) {
		String[] properties = new String[headMap.size()];

		Row titleRow = sheet.createRow(0);
		Cell titileCell = titleRow.createCell(0);
		titileCell.setCellStyle(titleStyle);
		titileCell.setCellValue(titile);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));
		int i = 0;
		String fieldkey = null;
		String fieldName = null;
		Row headerRow = sheet.createRow(1);
		int columnWidth = 0;
		int bytes = 0;
		for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
			fieldkey = iter.next();
			fieldName = headMap.get(fieldkey);
			properties[i] = fieldkey;
			bytes = fieldName.getBytes().length;
			columnWidth = bytes < minBytes ? minBytes : bytes;
			sheet.setColumnWidth(i, columnWidth * 256);
			headerRow.createCell(i).setCellValue(fieldName);
			headerRow.getCell(i).setCellStyle(headerStyle);
			i++;
		}
	}

	/**
	 * 导入公用方法
	 *
	 * @param inputStream excel流
	 * @param fileName    文件名
	 * @param mapIndex    导入的EXCEL映射对象
	 * @param titleRowNum 标题行数
	 * @return List<Map> 数据集
	 * @author linjie
	 */
	public static List<Map<String, Object>> readData(Workbook workBook, String fileName, String[] mapIndex,
			int titleRowNum) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Sheet sheet = workBook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum();
		if (rowNum < titleRowNum) {
			throw new Exception("excel中无可用的数据！");
		}
		readDateFromSheet(mapIndex, titleRowNum, rowNum, list, sheet);
		return list;
	}

	/**
	 * 导入公用方法
	 *
	 * @param inputStream excel流
	 * @param fileName    文件名
	 * @param mapIndex    导入的EXCEL映射对象
	 * @param titleRowNum 标题行数
	 * @return List<Map> 数据集
	 * @author linjie
	 */
	public static List<Map<String, Object>> readData(Workbook workBook, String fileName, String[] mapIndex,
			int titleRowNum, int lastRow) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Sheet sheet = workBook.getSheetAt(0);

		if (lastRow < titleRowNum) {
			throw new Exception("excel中无可用的数据！");
		}
		readDateFromSheet(mapIndex, titleRowNum, lastRow, list, sheet);
		return list;
	}

	/**
	 * 从sheet里面读取数据
	 * 
	 * @param mapIndex
	 * @param titleRowNum
	 * @param lastRow
	 * @param list
	 * @param sheet
	 */
	private static void readDateFromSheet(String[] mapIndex, int titleRowNum, int lastRow,
			List<Map<String, Object>> list, Sheet sheet) {
		for (int i = titleRowNum; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			int colNum = row.getLastCellNum();
			if (colNum > mapIndex.length) {
				colNum = mapIndex.length;
			}
			Map<String, Object> dataMap = new HashMap<String, Object>();
			int num = 0;
			for (int j = 0; j < colNum; j++) {
				Cell cell = row.getCell(j);
				if (cell == null) {
					dataMap.put(mapIndex[j], "");
					num++;
					continue;
				}
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					dataMap.put(mapIndex[j], cell.getStringCellValue());
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					dataMap.put(mapIndex[j], cell.getBooleanCellValue());
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					String cellValue = null;
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						cellValue = sFormat.format(date);
					} else {
						NumberFormat nf = NumberFormat.getInstance();
						nf.setGroupingUsed(false);// true时的格式：1,234,567,890
						nf.setMaximumFractionDigits(4); // 设置最多小数位为4位
						double acno = cell.getNumericCellValue();
						cellValue = nf.format(acno);
					}
					dataMap.put(mapIndex[j], cellValue + "");
					break;
				case XSSFCell.CELL_TYPE_FORMULA:
					dataMap.put(mapIndex[j], cell.getCellFormula());
					break;
				default:
					dataMap.put(mapIndex[j], "");
					num++;
					break;
				}
			}
			if (num == dataMap.size()) {
				continue;
			}
			list.add(dataMap);
		}
	}

	/**
	 * 将某个单元格的边框设置为黑色
	 * 
	 * @param workBook    工作薄
	 * @param recordCount 行最大值
	 * @param cellSize    单元格行结束最大值
	 */
	public static void setBlackColorStyle(Workbook workBook, int recordCount, int cellSize) {
		if (recordCount == 0) {
			return;
		}
		Sheet sheet = workBook.getSheetAt(0);
		recordCount = recordCount + 2;
		Row row = null;
		Cell cell = null;
		CellStyle cellStyle = workBook.createCellStyle();
		for (int i = 2; i < recordCount; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				for (int j = 0; j < cellSize; j++) {
					cell = row.getCell(j);
					if (cell != null) {
						ExcelUtils.setBlackBorderColorStyle(cellStyle);
						cell.setCellStyle(cellStyle);
					}
				}
			}
		}
	}

	/**
	 * 将某个单元格的边框设置为红色
	 * 
	 * @param workBook    工作薄
	 * @param recordCount 行最大值
	 * @param cellSize    单元格行结束最大值
	 */
	public static void setRedColorStyle(Workbook workBook, Map<Integer, Set<Integer>> invalidCellIndex) {
		if (invalidCellIndex == null || invalidCellIndex.isEmpty()) {
			return;
		}
		Sheet sheet = workBook.getSheetAt(0);
		Set<Integer> keySet = invalidCellIndex.keySet();
		Row row = null;
		Cell cell = null;
		CellStyle cellStyle = workBook.createCellStyle();
		Set<Integer> cellIndexSet = null;
		for (Integer rowIndex : keySet) {
			row = sheet.getRow(rowIndex);
			if (row != null) {
				cellIndexSet = invalidCellIndex.get(rowIndex);
				if (cellIndexSet != null && !cellIndexSet.isEmpty()) {
					for (Integer cellIndex : cellIndexSet) {
						cell = row.getCell(cellIndex);
						if (cell == null) {
							cell = row.createCell(cellIndex);
						}
						if (cell != null) {
							ExcelUtils.setBorderThinStyle(cellStyle);
							ExcelUtils.setRedBorderColorStyle(cellStyle);
							cell.setCellStyle(cellStyle);
						}
					}
				}
			}
		}

	}

}
