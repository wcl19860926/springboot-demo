package com.study.util;

import com.study.export.constants.Constants;
import com.study.export.csv.io.CsvBeanObjectReader;
import com.study.export.csv.io.ICsvBeanObjectReader;
import com.study.export.dto.FieldDto;
import com.study.export.util.HeadUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvImportHelper<T> {

    private static final Logger LOGGER = Logger.getLogger(CsvImportHelper.class);

    private Class<T> cls;
    //导出记录保存的文件
    private File file;

    private InputStream inputStream;

    //导入记录总条数，
    private long count;

    private int curentPage;

    private ICsvBeanObjectReader csvReader;

    //导出标题，及字段信息
    private FieldDto[] filedDtos;

    private String[] headTitles;

    /**
     * 是否读取完成
     */
    private boolean isEnd;


    public CsvImportHelper(String fileName, Class<T> cls) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("file not found !" + fileName);
        }
        this.inputStream = new FileInputStream(file);
        this.cls = cls;
        this.init();
    }


    public CsvImportHelper(InputStream inputStream, Class<T> cls) throws Exception {
        this.cls = cls;
        this.inputStream = inputStream;
        this.init();
    }


    private void init() throws Exception {
        this.count = 0;
        this.curentPage = 0;
        csvReader = new CsvBeanObjectReader(new InputStreamReader(inputStream, Constants.CSV_CHAR_SET), CsvPreference.EXCEL_PREFERENCE);
        this.filedDtos = HeadUtils.getImportHeadFieldDtos(cls, csvReader.getHeader(true));
        headTitles = HeadUtils.getHeadFieldTitles(filedDtos);

    }

    /**
     * 每次重特定文件里抓取多少行,可以多次抓取，只到文件结尾，
     * 使用者只需判，实际返回行数是，是否小想要抓取的行数
     * 或者调用 isEnd方法，判断是否结否
     *
     * @param size
     * @return
     * @throws Exception
     */
    public List<T> fectchBySize(int size) throws Exception {

        int pageCount = 0;
        List<T> objectList = new ArrayList<>();
        //如果已经到文件结尾则直接返回
        if (this.isEnd) {
            return objectList;
        }
        if (size <= 0) {
            return objectList;
        }
        T object = csvReader.read(cls, filedDtos);
        if (object == null) {
            isEnd = true;//已经文件结尾，则结束导出，并关闭输入流
            this.close();
        }
        pageCount++;
        if (!isEnd) {
            objectList.add(object);
            while (!isEnd && pageCount < size) {
                object = csvReader.read(cls, filedDtos);
                if (object == null) {
                    isEnd = true;//已经文件结尾，则结束导出，并关闭输入流
                    this.close();
                    break;
                }
                pageCount++;
                objectList.add(object);
            }
        }
        return objectList;
    }


    public void close() {

        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(csvReader);
    }

    /**
     * 返回是否解析完成
     *
     * @return
     */
    public boolean isEnd() {
        return this.isEnd;
    }

}
