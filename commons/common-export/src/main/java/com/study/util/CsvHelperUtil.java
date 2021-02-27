package com.study.util;

import com.study.export.constants.Constants;
import com.study.export.csv.io.CsvBeanObjectReader;
import com.study.export.csv.io.CsvBeanObjectWriter;
import com.study.export.csv.io.ICsvBeanObjectReader;
import com.study.export.csv.io.ICsvBeanObjectWriter;
import com.study.export.dto.FieldDto;
import com.study.export.util.HeadUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvHelperUtil {


    private static final Logger LOGGER = Logger.getLogger(CsvHelperUtil.class);

    /**
     * @param fileName
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> reader(String fileName, Class<T> cls) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            return reader(inputStream, cls);
        } catch (Exception e) {
            LOGGER.error("parse  a  csv file  error ", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }


    /**
     * @param inputStream
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> reader(InputStream inputStream, Class<T> cls) {
        ICsvBeanObjectReader inFile = null;
        List<T> objectList = new ArrayList<>();
        try {
            inFile = new CsvBeanObjectReader(new InputStreamReader(inputStream, Constants.CSV_CHAR_SET), CsvPreference.EXCEL_PREFERENCE);
            FieldDto[] filedDtos = HeadUtils.getImportHeadFieldDtos(cls, inFile.getHeader(true));
            T object = inFile.read(cls, filedDtos);
            while (object != null) {
                objectList.add(object);
                object = inFile.read(cls, filedDtos);
            }
        } catch (Exception e) {
            LOGGER.error("parse  a  csv file  error ", e);
        } finally {
            IOUtils.closeQuietly(inFile);
        }
        return objectList;
    }


    public static <T> File writeToFile(String fileName, List<T> listObject) {
        File csvFile = new File(fileName);
        ICsvBeanObjectWriter csvWriter = null;
        Writer writer = null;
        try {
            if (!csvFile.exists()) {
                csvFile.createNewFile();
            }
            if (CollectionUtils.isEmpty(listObject)) {
                return csvFile;
            }
            writer = new OutputStreamWriter(new FileOutputStream(csvFile), Constants.CSV_CHAR_SET);
            csvWriter = new CsvBeanObjectWriter(writer, CsvPreference.STANDARD_PREFERENCE);
            //取第一个对象的类信息
            FieldDto[] filedDtos = HeadUtils.getExplortHeadFieldDtos(listObject.get(0).getClass());
            String[] headTitles = HeadUtils.getHeadFieldTitles(filedDtos);
            csvWriter.writeHeader(headTitles);
            Iterator<T> it = listObject.iterator();
            T object = null;
            while (it.hasNext()) {
                object = it.next();
                csvWriter.write(object, filedDtos);
            }
            csvWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("write object to csv file failed ", e);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(csvWriter);
        }
        return csvFile;
    }
}
