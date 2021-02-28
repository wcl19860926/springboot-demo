package com.study.util;

import com.study.core.io.ByteArrayOutputStream;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {


    protected static final Logger logger = Logger.getLogger(HttpUtils.class);

    public static InputStream uploadEInvoicePdf(String url) {

        InputStream inputStream = null;
        HttpURLConnection httpConnection = null;
        try {
            URL invoiceURL = new URL(url);
            httpConnection = (HttpURLConnection) invoiceURL.openConnection();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpConnection.getInputStream();
                //随机生成文件名
                try (ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {
                    byteOutput.write(inputStream);
                }
            } else {
                logger.error("下载电子发票到本地失败！" + "httpCode : " + responseCode);
            }
        } catch (Exception e) {
            logger.error("下载电子发票到本地失败！", e);
        } finally {//关闭资源
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return inputStream;
    }
}
