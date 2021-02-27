package com.study.util;

import com.study.export.constants.Constants;
import com.study.export.csv.io.CsvBeanObjectWriter;
import com.study.export.csv.io.ICsvBeanObjectWriter;
import com.study.export.dto.FieldDto;
import com.study.export.util.HeadUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

public class CsvExportHelper<T> {

    private static final Logger LOGGER = Logger.getLogger(CsvExportHelper.class);

    private Class<T> cls;
    //导出记录保存的文件
    private File resultFile;

    //导出记录总条数，
    private long count;
    //
    private int curentPage;

    private ICsvBeanObjectWriter csvWriter;

    //导出标题，及字段信息
    private FieldDto[] filedDtos;

    private String[] headTitles;

    public CsvExportHelper(Class<T> cls) throws Exception {
        this.cls = cls;
        this.count = 0;
        this.curentPage = 0;
        this.filedDtos = HeadUtils.getExplortHeadFieldDtos(cls);
        resultFile = File.createTempFile("export_csv", ".csv");
        headTitles = HeadUtils.getHeadFieldTitles(filedDtos);
        csvWriter = new CsvBeanObjectWriter(new OutputStreamWriter(new FileOutputStream(resultFile),
                Constants.CSV_CHAR_SET), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(headTitles);//写入头文件
    }


    public int exportByPageList(List<T> objList) throws Exception {
        int pageCount = 0;
        Iterator<T> it = objList.iterator();
        T object;
        while (it.hasNext()) {
            object = it.next();
            csvWriter.write(object, filedDtos);
            count++;
            pageCount++;
        }
        csvWriter.flush();
        curentPage++;
        return pageCount;
    }


    //完成写入，返回导出的文件引用
    public File getResult() {
        IOUtils.closeQuietly(csvWriter);
        return resultFile;
    }

}
