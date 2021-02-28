package com.study.oauth2.server.util;


import com.study.oauth2.server.constants.Constant;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;


public class ResponseUtils {
    private  static Logger logger =  LoggerFactory.getLogger(ResponseUtils.class);

    public static  <T> void writeStringToResponse(HttpServletResponse response, T t) {
        PrintWriter writer = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            writer = response.getWriter();
            writer.write(t.toString());
        } catch (IOException e) {
            logger.error("向额户端输出息失败", e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }



    public void writeExcelDownResp(HttpServletResponse response, Workbook workbook, String title) throws IOException {
        String fileName = URLEncoder.encode(title + "." + Constant.EXCEL_FILE_EXTENTION_XLSX, "UTF-8");
        //设置要下载的文件的名称
        response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
        //通知客服文件的MIME类型
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        //获取文件的路径
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
