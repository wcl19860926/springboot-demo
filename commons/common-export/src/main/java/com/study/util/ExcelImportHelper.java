package com.study.util;


import com.study.export.dto.FieldDto;
import com.study.export.excel.converter.str.*;
import com.study.export.util.ClassUtils;
import com.study.export.util.HeadUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

public class ExcelImportHelper<T> {

    private static final int PAGESIZE = 200;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportHelper.class);

    private Map<Integer, String> shareStringMap;

    private Consumer callBack;

    private static int startRowIndex;


    public ExcelImportHelper(Consumer callBack) {
        this.callBack = callBack;
    }

    public void pageListCallBack(List<T> pageListDate) {
        callBack.accept(pageListDate);
    }


    /**
     * @param file
     * @param cls
     * @param startRowIndex
     * @return
     * @throws Exception
     */
    public void readData(File file, Class<T> cls, int startRowIndex) throws Exception {
        this.startRowIndex = startRowIndex;
        if (file == null || !file.exists()) {
            return;
        }
        InputStream in = new FileInputStream(file);
        readData(in, cls, startRowIndex);
    }

    /**
     * @param in
     * @param cls
     * @param startRowIndex
     * @return
     * @throws Exception
     */
    public void readData(InputStream in, Class<T> cls, int startRowIndex) throws Exception {
        List<InputStream> sheetInputStreamList = this.getAllExcelSheepInputStream(in);
        if (!CollectionUtils.isEmpty(sheetInputStreamList)) {
            processOfferSheetData(shareStringMap, sheetInputStreamList.get(0), cls);

        }

    }

    /**
     * @param in
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private List<InputStream> getAllExcelSheepInputStream(InputStream in) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        if (in == null) {
            return null;
        }
        OPCPackage opcPackage = OPCPackage.open(in);
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        InputStream sharedStringsInputStream = xssfReader.getSharedStringsData();
        shareStringMap = processOfferShareData(sharedStringsInputStream);
        XSSFReader.SheetIterator iterator = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        List<InputStream> imputStreamList = new ArrayList<InputStream>();
        while (iterator.hasNext()) {
            imputStreamList.add(iterator.next());
        }
        return imputStreamList;
    }

    /**
     * 解析ShareString
     */
    public Map<Integer, String> processOfferShareData(InputStream sheetInputStream)
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
    public void processOfferSheetData(Map<Integer, String> shareStringMap, InputStream sheetInputStream, Class<T> cls)
            throws Exception {
        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        List<FieldDto> FieldDtos = HeadUtils.getExportFiledDto(cls);
        XSSFSheetHandler handler = new XSSFSheetHandler(this, cls, shareStringMap, FieldDtos, startRowIndex);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);

    }

    /**
     * @param <T>
     */
    static class XSSFSheetHandler<T> extends DefaultHandler {

        private static final String ROW_TAG = "row";
        private static final String CELL_TAG = "c";//此标记为单元格
        private static final String SHARE_OR_NUMBER_STRING_TAG = "v";//表示共享字符串或为 number 时取Value
        private static final String INLINE_STRING_TAG = "t";//当元素c 的 t 属性值为inlingStr时，从标签<si><t>value</t></si>
        private static final String SHARE_STRING_TYPE_ATTR = "t";//当元素c 的属性名t
        private static final String SHARE_STRING_TYPE_ATTR_VALUE = "s";//当元素c 的 t 属性值为s时代表值存在shareString.xml


        private T rowObject;// 存每一行数据

        private Class<T> cls;

        private int sumRows = 0;

        private boolean isShare = false;

        private boolean isCell = false;


        private int cellIndex = 0;
        // 单个单元格的值
        private StringBuffer cellValue = null;
        //所有shareStringMap
        private final Map<Integer, String> shareStringMap;

        private FieldDto[] fieldDtos;
        private final List<FieldDto> exportFields;
        //数据起始行
        private final int startRow;

        private List<T> pageListData = new ArrayList<>(PAGESIZE);

        private List<String> titleList = new ArrayList<>();

        private int rowIndex;

        ExcelImportHelper helper;

        //实例化必须提供字段key的数组，和数据的起始行
        //读取sheet数据时将跳过小于startRow的行，并且忽略超出dataFields长度的列
        public XSSFSheetHandler(ExcelImportHelper helper, Class<T> cls, Map<Integer, String> shareStringMap, List<FieldDto> exportFields, int startRow) {
            this.cls = cls;
            this.shareStringMap = shareStringMap;
            this.exportFields = exportFields;
            this.startRow = startRow;
            this.helper = helper;
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (ROW_TAG.equals(name)) {
                rowObject = ClassUtils.getInstanceByClass(cls);
                cellIndex = 0;
                rowIndex++;
            }

            if (CELL_TAG.equals(name)) {
                String typeAttr = attributes.getValue(SHARE_STRING_TYPE_ATTR);
                if (StringUtils.isNotBlank(typeAttr)) {
                    if (SHARE_STRING_TYPE_ATTR_VALUE.equals(typeAttr)) {
                        isShare = true;
                    }
                }
            }

            if (SHARE_OR_NUMBER_STRING_TAG.equals(name)) {
                isCell = true;
                cellValue = new StringBuffer("");
            }

            if (INLINE_STRING_TAG.equals(name)) {
                isCell = true;
                cellValue = new StringBuffer("");
            }
            if (ROW_TAG.equals(name)) {
                if (rowIndex == startRow + 1) {
                    try {
                        this.fieldDtos = HeadUtils.getImportHeadFieldDtos(this.cls, titleList.toArray(new String[titleList.size()]));
                    } catch (Exception e) {
                        LOGGER.error("解析表头失败", e);
                        throw new SAXException(e);
                    }

                }
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            //读取的是headTitle行
            if (rowIndex == startRow) {
                if (isCell) {
                    String value = cellValue.toString();
                    if (isShare) {
                        value = shareStringMap.get(Integer.parseInt(value));
                    }
                    titleList.add(value);
                }

            }
            if (rowIndex > startRow) {
                if (isCell) {
                    String value = cellValue.toString();
                    if (isShare) {
                        value = shareStringMap.get(Integer.parseInt(value));
                    }
                    if (value != null) {
                        if (fieldDtos[cellIndex] != null) {
                            FieldDto dto = fieldDtos[cellIndex];
                            Method setMethod = dto.getSetMethod();
                            StrToObjectConverter converter = defaultConverterMap.get(dto.getType().getTypeName());
                            try {
                                if (converter != null) {
                                    setMethod.invoke(rowObject, converter.toTargetValue(dto, value));
                                } else {
                                    setMethod.invoke(rowObject, value);
                                }
                            } catch (Exception e) {
                                throw new SAXException("set  value  " + value + " for  field " + dto.getFieldName() + "fialed ! ", e);
                            }
                        }
                    }
                }
                if (ROW_TAG.equals(qName)) {
                    if (sumRows % PAGESIZE == 0 && sumRows > 0) {
                        this.helper.pageListCallBack(pageListData);
                        pageListData = new ArrayList<>(PAGESIZE);
                    }
                    pageListData.add(rowObject);
                    sumRows++;
                }
            }
            if (SHARE_OR_NUMBER_STRING_TAG.equals(qName)) {
                isCell = false;
                isShare = false;
            }

            if (INLINE_STRING_TAG.equals(qName)) {
                isCell = false;
            }
            if (CELL_TAG.equals(qName)) {
                cellIndex++;

            }

        }


        @Override
        public void endDocument() throws SAXException {
            this.helper.pageListCallBack(pageListData);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isCell) {
                cellValue.append(ch, start, length);
            }
        }

        public int getSumRows() {
            return sumRows;
        }

    }

    static class XSSFShareStringHandler extends DefaultHandler {

        private static final String VALUE_TAG = "t";

        private Map<Integer, String> shareStringMap = new HashMap<>();

        private int keyIndex = 0;

        private boolean isShareString = false;
        // 值
        private StringBuffer shareString = null;

        public XSSFShareStringHandler() {

        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (VALUE_TAG.equals(name)) {
                isShareString = true;
                shareString = new StringBuffer("");
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (VALUE_TAG.equals(qName)) {
                shareStringMap.put(keyIndex++, shareString.toString());
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isShareString) {
                shareString.append(ch, start, length);
            }
        }

        public Map<Integer, String> getShareStringMap() {
            return shareStringMap;
        }
    }


    /**
     * 默认的转换器
     */
    private static Map<String, StrToObjectConverter> defaultConverterMap;

    static {
        defaultConverterMap = new HashMap<>();
        defaultConverterMap.put(Long.class.getTypeName(), new StrToLong());
        defaultConverterMap.put(long.class.getTypeName(), new StrToBaseLong());
        defaultConverterMap.put(Integer.class.getTypeName(), new StrToInteger());
        defaultConverterMap.put(int.class.getTypeName(), new StrToInt());
        defaultConverterMap.put(boolean.class.getTypeName(), new StrToBaseBoolean());
        defaultConverterMap.put(Float.class.getTypeName(), new StrToFloat());
        defaultConverterMap.put(float.class.getTypeName(), new StrToBaseFloat());
        defaultConverterMap.put(double.class.getTypeName(), new StrToBaseDouble());
        defaultConverterMap.put(Double.class.getTypeName(), new StrToDouble());
        defaultConverterMap.put(BigDecimal.class.getTypeName(), new StrToBigDecimal());
        defaultConverterMap.put(Date.class.getTypeName(), new StrToDate());
        defaultConverterMap.put(String.class.getTypeName(), new StrToString());
        defaultConverterMap.put(Boolean.class.getTypeName(), new StrToBoolean());


    }


}
