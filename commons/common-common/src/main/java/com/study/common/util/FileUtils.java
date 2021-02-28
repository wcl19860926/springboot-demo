package com.study.common.util;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.input.ClosedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 文件的工具类
 */
public final class FileUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static final int EOF = -1;

    private static final int SKIP_BUFFER_SIZE = 2048;


    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {

    }


    /**
     * 将输入流转换为字节数组
     *
     * @param in
     * @return
     */
    public byte[] toBytes(InputStream in) {
        byte[] bytes = null;
        if (in == null) {
            throw new IllegalArgumentException("InputStream argument cannot be null.");
        } else {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(512);) {
                byte[] buffer = new byte[512];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                bytes = out.toByteArray();
            } catch (IOException e) {
                logger.error("将文件转换成字节数据失败！", e);
            }
            return bytes;
        }
    }


    /**
     * 将byte[] 数组的集合，转换为输入流
     *
     * @param byteArrList
     * @return
     */

    public InputStream toInputStream(List<byte[]> byteArrList) {

        if (byteArrList == null && byteArrList.isEmpty()) {
            return new ClosedInputStream();
        }
        List<ByteArrayInputStream> list = new ArrayList<ByteArrayInputStream>(byteArrList.size());
        for (byte[] buf : byteArrList) {
            int c = buf.length;
            list.add(new ByteArrayInputStream(buf, 0, c));

        }
        return new SequenceInputStream(Collections.enumeration(list));
    }


    /**
     * 将一个输入流，返回另一个输入流
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return org.apache.commons.io.output.ByteArrayOutputStream.toBufferedInputStream(input);
    }


    /**
     * 将一个输入流，返回另一个输入流
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        return org.apache.commons.io.output.ByteArrayOutputStream.toBufferedInputStream(input, size);
    }


    /**
     * 从一个输入流，返回另一个输入流
     *
     * @param reader
     * @return
     */
    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }


    public static BufferedReader toBufferedReader(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }


    public static BufferedWriter buffer(final Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }


    /**
     * 将输入流复制到输出流
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copy(final InputStream input, final OutputStream output)
            throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[SKIP_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    /**
     * 将将字节输入流复制到字节输出流
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copy(final Reader input, final Writer output) throws IOException {
        long count = 0;
        int n;
        char[] buffer = new char[SKIP_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    public static void copy(final InputStream input, final Writer output, final Charset inputEncoding)
            throws IOException {
        final InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(inputEncoding));
        copy(in, output);
    }





    public static long skip(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        byte[] SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
        long remain = toSkip;
        while (remain > 0) {
            final long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }


    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, SKIP_BUFFER_SIZE));
        long remain = toSkip;
        while (remain > 0) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int) Math.min(remain, SKIP_BUFFER_SIZE));
            final int n = input.read(skipByteBuffer);
            if (n == EOF) {
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static long skip(final Reader input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        char[] SKIP_CHAR_BUFFER = new char[SKIP_BUFFER_SIZE];
        long remain = toSkip;
        while (remain > 0) {
            final long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static int read(final Reader input, final char[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.read(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }
}
