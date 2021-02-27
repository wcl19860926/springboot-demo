package com.study.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelExportHelper<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportHelper.class);

	/**
	 * 每每个sheet最大行数
	 */
	private static final int MAX_SHEET_RECORDS_COUNT = 60000;

	private ExcelsheetWriter excelSheetWriter;

	/**
	 * 当前写入行
	 */
	private File currentSheet;

	private Map<String, String> header;

	/**
	 * 写入的Sheet文件
	 */
	private List<File> sheetFiles;

	/**
	 * 写入的总记录数
	 */
	private int sumRecordCount;

	/**
	 * 写入结果的结果文件
	 */
	private File resultWorkBookFile;

	/**
	 * 文件标题
	 */
	private String titile;

	/**
	 * 做一些清理， 删除 多余的临时文件
	 * 
	 * @throws Throwable
	 */
	private void close() throws Exception {
		if (currentSheet != null) {
			if (currentSheet.exists()) {
				FileUtils.deleteQuietly(currentSheet);
			}
		}
		if (sheetFiles != null) {
			for (File file : sheetFiles) {
				FileUtils.deleteQuietly(file);
			}
		}
	}

	public ExcelExportHelper(Map<String, String> header, String titile) throws Exception {
		super();
		this.titile = titile;
		this.sheetFiles = new ArrayList<File>();
		this.sumRecordCount = 0;
		this.header = header;
		try {
			this.resultWorkBookFile = File.createTempFile("workbook", "xlsx");
		} catch (IOException e) {
			throw new Exception("导出创建临时文件失败！", e);
		}
	}

	/**
	 * 写入Excel表头
	 * 
	 * @throws IOException
	 */
	private void benginExport() throws Exception {
		if (currentSheet != null) {
			this.endExport();
		}
		this.currentSheet = File.createTempFile("sheet", "xml");
		this.sheetFiles.add(currentSheet);
		this.excelSheetWriter = new ExcelsheetWriter(
				new OutputStreamWriter(new FileOutputStream(currentSheet), "UTF-8"), this.header, this.titile);
		excelSheetWriter.beginSheet();
		excelSheetWriter.insertTitileRow();
		excelSheetWriter.insertHeaderRow();

	}

	/**
	 * 分页查询写入， 当结束时调 用getExportResult得到结果文件
	 * 
	 * @param listData
	 * @throws Exception
	 */
	public void writeDataToSheet(List<T> listData) throws Exception {
		if (listData != null && !listData.isEmpty()) {
			Class<?> cls = listData.get(0).getClass();
			for (T t : listData) {
				if (sumRecordCount % MAX_SHEET_RECORDS_COUNT == 0) {
					this.benginExport();
				}
				sumRecordCount++;
				excelSheetWriter.insertDataRow(t, cls, 0);
			}
		}

	}

	/**
	 * 结束写入当前sheet
	 * 
	 * @throws Exception
	 */
	private void endExport() throws Exception {
		try {
			excelSheetWriter.endSheet();
		} catch (IOException e) {
			throw new Exception(e);
		}

	}

	/**
	 * 用完结果文件请删除
	 * 
	 * @return 返回结果文件
	 * @throws Exception
	 */
	public File getExportResult() throws Exception {
		if (this.sheetFiles == null || sheetFiles.isEmpty()) {
			this.benginExport();// 导出一个无业务数据的Excel
		}
		this.endExport();
		XSSFWorkbook workBook = this.createWorkBook();
		int size = this.sheetFiles.size();
		List<String> sheetEntryName = new ArrayList<String>();
		XSSFSheet sheet = null;
		String sheetRef = null;
		for (int i = 0; i < size; i++) {
			sheet = workBook.createSheet();
			sheetRef = sheet.getPackagePart().getPartName().getName();
			sheetEntryName.add(sheetRef.substring(1));
		}
		File tempWorkSheetFile = File.createTempFile("temp", "xlsx");
		FileOutputStream output = new FileOutputStream(tempWorkSheetFile);
		workBook.write(output);
		output.close();
		putSheetToExcelFile(tempWorkSheetFile, this.sheetFiles, sheetEntryName);
		this.close();// 做一些清理，删除临文件
		return this.resultWorkBookFile;

	}

	/**
	 * 创建XSSFWorkbook 并添加样试
	 * 
	 * @return
	 */
	private XSSFWorkbook createWorkBook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		ExcelUtils.createHeaderStyle(workbook);
		ExcelUtils.createTitileStyle(workbook);
		ExcelUtils.createCellStyle(workbook);
		return workbook;
	}

	/**
	 * 不是分页导出， 将dataList写入Excel 并返回结果 超过6000行，会自动分Sheet处理。
	 * 
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	public File exportByAuto(List<T> dataList) throws Exception {
		this.writeDataToSheet(dataList);
		return this.getExportResult();

	}

	/**
	 * 将数据结果文件，写入到模板结果文件中
	 * 
	 * @param tempWorkSheetFile
	 * @param sheetDataFiles
	 * @param sheetName
	 * @throws Exception
	 */
	private void putSheetToExcelFile(File tempWorkSheetFile, List<File> sheetDataFiles, List<String> sheetName)
			throws Exception {
		FileOutputStream out = null;
		ZipOutputStream zos = null;
		ZipFile zip = null;
		try {
			zip = new ZipFile(tempWorkSheetFile);
			out = new FileOutputStream(this.resultWorkBookFile);
			zos = new ZipOutputStream(out);
			@SuppressWarnings("unchecked")
			Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
			while (en.hasMoreElements()) {
				ZipEntry ze = en.nextElement();
				if (!sheetName.contains(ze.getName())) {
					zos.putNextEntry(new ZipEntry(ze.getName()));
					InputStream is = zip.getInputStream(ze);
					IOUtils.copy(is, zos);
					is.close();
				}
			}
			int i = 0;
			for (File file : sheetDataFiles) {
				zos.putNextEntry(new ZipEntry(sheetName.get(i)));
				i++;
				InputStream is = new FileInputStream(file);
				IOUtils.copy(is, zos);
				is.close();
			}
			zos.close();
			zip.close();
		} catch (Exception e) {
			throw new Exception("导出创建临时文件失败！", e);
		} finally {
			if (ObjectUtils.isNotBlank(zos)) {
				IOUtils.closeQuietly(zos);
			}
			if (ObjectUtils.isNotBlank(zip)) {
				IOUtils.closeQuietly(zip);
			}
			if (ObjectUtils.isNotBlank(out)) {
				IOUtils.closeQuietly(out);
			}
		}
		FileUtils.deleteQuietly(tempWorkSheetFile);
	}

	/**
	 * 
	 * 将数据写入到Sheet.xml中
	 */
	static class ExcelsheetWriter {
		private final Writer _out;
		public static final int DEFAULT_COLOUMN_WIDTH = 17;
		Map<String, String> header;
		String[] headerKey;
		private String title;
		String[] headerName;
		private int _rownum;
		private int records;

		public ExcelsheetWriter(Writer out, Map<String, String> headMap, String title) {
			_out = out;
			this.title = title;
			header = headMap;
			records = 0;
			headerKey = new String[headMap.size()];
			headerName = new String[headMap.size()];
			Set<String> keySet = header.keySet();
			int i = 0;
			for (String key : keySet) {
				headerKey[i] = key;
				headerName[i] = header.get(key);
				i++;
			}
		}

		/**
		 * 开始Sheet
		 * 
		 * @throws IOException
		 */
		public void beginSheet() throws IOException {
			_out.write("<?xml version=\"1.0\" encoding=\"" + "UTF-8" + "\"?>"
					+ "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
					+ " <dimension ref=\"A1\"/><sheetViews><sheetView workbookViewId=\"0\" tabSelected=\"true\"/></sheetViews><sheetFormatPr defaultRowHeight=\"15.0\"/>");
			_out.write("<cols>");
			for (int i = 0; i < headerName.length; i++) {
				String header = headerName[i];
				float width = header.length() * 2.7f;
				if (width < DEFAULT_COLOUMN_WIDTH) {
					width = DEFAULT_COLOUMN_WIDTH;
				}
				_out.write("<col min=\"" + (i + 1) + "\" max=\"" + (i + 1) + "\" width=\"" + width
						+ "\" bestFit=\"1\" customWidth=\"1\"/>");
			}
			_out.write("</cols>");
			_out.write("<sheetData>");
		}

		/**
		 * excel 行列对应的CellReference -- A1 , D1
		 * 
		 * @param row
		 * @param col
		 * @return
		 */

		private static String getRefName(int row, int col) {
			return new CellReference(row, col).formatAsString();
		}

		/**
		 * 获取String类型的值
		 * 
		 * @param o
		 * @return
		 */
		private static String getStringValue(Object o) {
			if (o == null) {
				return "";
			}
			if (o instanceof Date) {
				SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sFormat.format(o);
			}
			return o.toString();
		}

		/**
		 * 结束当前Sheet
		 * 
		 * @throws IOException
		 */
		public void endSheet() throws IOException {
			_out.write("</sheetData>");
			String start = getRefName(0, 0);
			String end = getRefName(0, this.headerName.length - 1);
			_out.write("<mergeCells><mergeCell ref=\"" + start + ":" + end + "\"/></mergeCells>");
			_out.write(
					"<pageMargins bottom=\"0.75\" footer=\"0.3\" header=\"0.3\" left=\"0.7\" right=\"0.7\" top=\"0.75\"/></worksheet>");
			_out.flush();
			IOUtils.closeQuietly(_out);
		}

		/**
		 * 插入标题
		 * 
		 * @param titileName
		 * @throws IOException
		 */
		public void insertTitileRow() throws IOException {
			this.insertRowBenin(0);
			String ref = getRefName(0, 0);
			_out.write("<c r= \"" + ref + "\" s=\"1\" t=\"inlineStr\"><is><t>" + XmlUtils.encoderXML(title)
					+ "</t></is></c>");
			endRow();
		}

		/**
		 * 插入表头
		 * 
		 * @param headerMap
		 * @throws IOException
		 */
		public void insertHeaderRow() throws IOException {
			this.insertRowBenin(1);
			String ref = null;
			for (int i = 0; i < headerName.length; i++) {
				ref = getRefName(1, i);
				_out.write("<c r= \"" + ref + "\"  s=\"2\" t=\"inlineStr\"><is><t>" + XmlUtils.encoderXML(headerName[i])
						+ "</t></is></c>");
			}
			endRow();
		}

		/**
		 * 插入一行数据。
		 * 
		 * @param headerMap
		 * @throws IOException
		 * @throws SecurityException
		 * @throws NoSuchMethodException
		 */
		public void insertDataRow(Object obj, Class<?> cls, int colIndex) throws IOException, Exception {
			_rownum++;
			this.insertRowBenin(_rownum);
			Object value = null;
			Method method = null;
			int i = colIndex;
			for (String key : headerKey) {
				try {
					method = cls.getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length()));
					value = method.invoke(obj);
					// insertCell(_rownum, i,XmlUtils.encoderXML(
					// getStringValue(value)), 3);
					createCell(_rownum, i, value);
				} catch (Exception e) {
					LOGGER.error("get value by  getMethod failed !", e);
				}
				i++;
			}
			this.endRow();

		}

		/**
		 * Insert a new row
		 * 
		 * @param rownum 0-based row number
		 */
		private void insertRowBenin(int rownum) throws IOException {
			_out.write("<row r=\"" + (rownum + 1) + "\">\n");
			this._rownum = rownum;
		}

		/**
		 * Insert row end
		 */
		private void endRow() throws IOException {
			_out.write("</row>\n");
		}

		/**
		 * 创建 一个单元格
		 * 
		 * @param rowIndex    行索引
		 * @param columnIndex 列索引
		 * @param value       写入的值
		 * @param styleIndex  样试索引
		 * @throws IOException 异常
		 */

		private void insertCell(int rowIndex, int columnIndex, String value, int styleIndex) throws IOException {
			String ref = new CellReference(rowIndex, columnIndex).formatAsString();
			_out.write("<c r=\"" + ref + "\" t=\"inlineStr\"");
			_out.write(" s=\"" + styleIndex + "\"");
			_out.write(">");
			_out.write("<is><t>" + value + "</t></is>");
			_out.write("</c>");
		}

		private void createCell(int rowIndex, int columnIndex, Object value) throws IOException {
			if (value instanceof Number) {
				if (value instanceof Double) {
					NumberFormat nf = NumberFormat.getInstance();
					nf.setGroupingUsed(false);// 设转置分组1,234,567,890
					nf.setMaximumFractionDigits(2); // 设置两个小数位
					insertNumberCell(rowIndex, columnIndex, nf.format(value), 3);
				} else {
					insertNumberCell(rowIndex, columnIndex, String.valueOf(value), 3);
				}
			} else {
				insertCell(_rownum, columnIndex, XmlUtils.encoderXML(getStringValue(value)), 3);
			}
		}

		/**
		 * 创建 一个单元格
		 * 
		 * @param rowIndex    行索引
		 * @param columnIndex 列索引
		 * @param value       写入的值
		 * @param styleIndex  样试索引
		 * @throws IOException 异常
		 */

		private void insertNumberCell(int rowIndex, int columnIndex, String value, int styleIndex) throws IOException {
			String ref = new CellReference(rowIndex, columnIndex).formatAsString();
			_out.write("<c r=\"" + ref + "\" t=\"n\"");
			_out.write(" s=\"" + styleIndex + "\"");
			_out.write(">");
			_out.write("<v>" + value + "</v>");
			_out.write("</c>");
		}

		public int getRecords() {
			return records;
		}

	}

}
