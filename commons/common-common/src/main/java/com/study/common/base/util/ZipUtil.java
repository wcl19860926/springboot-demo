package com.study.common.base.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by xiquee.com on 2017/6/19.
 */
public class ZipUtil {
    /**
     * @param sourceDir  源文件路劲
     * @param zipFileDir 目标文件路劲
     */
    public void doZip(String sourceDir, String zipFileDir) {

        ZipOutputStream zos = null;
        try {
            File file = new File(sourceDir);
            File zipFile = new File(zipFileDir);

            // 创建写出流
            try (OutputStream os = new FileOutputStream(zipFile)) {
                try (BufferedOutputStream bos = new BufferedOutputStream(os)) {
                    zos = new ZipOutputStream(bos);
                }
            }

            // 获取目录
            String basePath = "";
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }

            zipFile(file, basePath, zos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.closeEntry();
                    zos.close();
                    zos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * @param file     源文件
     * @param basePath 基础路劲
     * @param zos      写出流
     */
    private void zipFile(File file, String basePath, ZipOutputStream zos)
            throws IOException {

        File[] files;
        if (file.isDirectory()) {
            files = file.listFiles();
        } else {
            files = new File[1];
            files[0] = file;
        }

        InputStream is = null;
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            // 遍历
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        pathName = f.getPath().substring(basePath.length() + 1)
                                + "/";
                        zos.putNextEntry(new ZipEntry(pathName));
                        zipFile(f, basePath, zos);
                    } else {
                        pathName = f.getPath().substring(basePath.length() + 1);
                        is = new FileInputStream(f);
                        try(BufferedInputStream bis = new BufferedInputStream(is)) {
                            zos.putNextEntry(new ZipEntry(pathName));
                            while ((length = bis.read(buf)) > 0) {
                                zos.write(buf, 0, length);
                            }
                        }
                    }
                }
            }

        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
        }

    }


}
