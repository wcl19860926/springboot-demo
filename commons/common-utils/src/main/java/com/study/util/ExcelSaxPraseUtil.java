package com.study.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析Excel 这个类提供较底层的方法，消耗内存较小
 * 
 * @author Administrator
 * 
 */
public class ExcelSaxPraseUtil {

	private Map<Integer,String> shareStringMap;
	
	
	
	/**
	 * 
	 * @param file 导入的文件
	 * @param dataFields   Excel列对应的字段key
	 * @param startRowIndex    数据开始行
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> readData(File file, String[] dataFields, int startRowIndex) throws Exception{
		if (file == null || !file.exists()) {
			return null;
		}
		InputStream in = new FileInputStream(file);
		return readData(in, dataFields, startRowIndex);
	}

	/**
	 * 
	 * @param in 导入的文件流
	 * @param dataFields   Excel列对应的字段key
	 * @param startRowIndex    数据开始行
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> readData(InputStream in, String[] dataFields, int startRowIndex) throws Exception{
		List<InputStream> inSlist= this.getAllExcelData(in); 
		if(inSlist != null && inSlist.size() > 0){
			List<Map<String, String>> cellList = this.processOfferSheetData(shareStringMap, inSlist.get(0), dataFields, startRowIndex);
			return cellList;
		}
		return null;
	}

	/**
	 * 
	 * @param InputStream in
	 * @return
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public List<InputStream> getAllExcelData(InputStream in) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
		if(in == null){
			return null;
		}
		OPCPackage opcPackage = OPCPackage.open(in);
		XSSFReader xssfReader = new XSSFReader(opcPackage);
		InputStream sharedStringsInputStream = xssfReader.getSharedStringsData();
		shareStringMap = processOfferShareData(sharedStringsInputStream);
		
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		List<InputStream> imputStreamList = new ArrayList<InputStream>();
		while (iter.hasNext()) {
			imputStreamList.add(iter.next());
		}
		return imputStreamList;
	}

	/**
	 * 解析ShareString
	 */
	public Map<Integer,String> processOfferShareData(InputStream sheetInputStream)
			throws IOException, ParserConfigurationException, SAXException {
		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		XSSFShareStringHandler handler = new XSSFShareStringHandler();
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);
		return handler.getShareStringMap();
	}

	/**
	 * 解析Sheet
	 */
	public List<Map<String, String>> processOfferSheetData(Map<Integer,String> shareStringMap, InputStream sheetInputStream, String[] dataFields, int startRowIndex)
			throws IOException, ParserConfigurationException, SAXException {
		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		XSSFSheetHandler handler = new XSSFSheetHandler(shareStringMap, dataFields, startRowIndex);
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);
		return handler.getRows();
	}

	static class XSSFSheetHandler extends DefaultHandler {

		private static final String ROW_TAG = "row";
		private static final String CELL_TAG = "t";//此标记直接可以拿到值
		private static final String CELL_SHARE_TAG = "v";//此标记的值来自于shareString

		private Map<String,String> rowData;// 存每一行数据

		private List<Map<String, String>> rows = new ArrayList<>();//单个sheet的所有数据

		private int sumRows = 0;

		private boolean isCell = false;

		private int cellIndex = 0;
		// 单个单元格的值
		private StringBuffer cellValue = null;
		//所有shareStringMap
		private final Map<Integer,String> shareStringMap;
		//所有需要读取的字段key值
		private final String[] dataFields;
		//数据起始行
		private final int startRow;

		//实例化必须提供字段key的数组，和数据的起始行 
		//读取sheet数据时将跳过小于startRow的行，并且忽略超出dataFields长度的列
		public XSSFSheetHandler(Map<Integer,String> shareStringMap, String[] dataFields, int startRow) {
			this.shareStringMap = shareStringMap;
			this.dataFields = dataFields;
			this.startRow = startRow;
		}

		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if (sumRows >= startRow && ROW_TAG.equals(name)) {
				rowData = new HashMap<>();
				cellIndex = 0;
			}
			
			if (CELL_TAG.equals(name) || CELL_SHARE_TAG.equals(name)) {
				isCell = true;
				cellValue = new StringBuffer("");
			} else {
				isCell = false;
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			//如果行读取标记大于等于数据起始行，开始写数据，并且要满足列标记小于字段key数组的长度，
			if(rowData != null && cellIndex < dataFields.length){
				if(CELL_TAG.equals(qName) || CELL_SHARE_TAG.equals(qName)){
					String value = cellValue.toString();
					if(CELL_SHARE_TAG.equals(qName)){
						value = shareStringMap.get(Integer.parseInt(value));
					}
					rowData.put(dataFields[cellIndex], value);
					cellIndex++;
				}
			}
			if(cellValue != null){
				System.out.println(cellValue.toString());
			}
			if (ROW_TAG.equals(qName)) {
				if(sumRows >= startRow){ 
					rows.add(rowData);
				}
				sumRows++;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (isCell) {
				cellValue.append(ch, start, length);
			}
		}

		public List<Map<String,String>> getRows() {
			return rows;
		}

		public int getSumRows() {
			return sumRows;
		}

	}

	static class XSSFShareStringHandler extends DefaultHandler {

		private static final String VALUE_TAG = "t";

		private Map<Integer,String> shareStringMap = new HashMap<>();

		private int keyIndex = 0;
		
		private boolean isShareString = false;
		// 值
		private StringBuffer shareString = null;

		public XSSFShareStringHandler() {

		}

		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if(VALUE_TAG.equals(name)){
				isShareString = true;
				shareString = new StringBuffer("");
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (VALUE_TAG.equals(qName)) {
				shareStringMap.put(keyIndex ++, shareString.toString());
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if(isShareString){
				shareString.append(ch, start, length);
			}
		}

		public Map<Integer, String> getShareStringMap() {
			return shareStringMap;
		}
	}
}