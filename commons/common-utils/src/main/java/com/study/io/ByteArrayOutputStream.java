package com.study.io;

import org.apache.commons.io.input.ClosedInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ByteArrayOutputStream extends OutputStream {


    @Override
    public void write(int b) throws IOException {
        int inBufferPos = count - filledBufferSum;
        if (inBufferPos == currentBuffer.length) {
            needNewBuffer(count + 1);
            inBufferPos = 0;
        }
        currentBuffer[inBufferPos] = (byte) b;
        count++;
    }

    /**
     * A singleton empty byte array.
     */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /**
     * The list of buffers, which grows and never reduces.
     */
    private final List<byte[]> buffers = new ArrayList<byte[]>();
    /**
     * The index of the current buffer.
     */
    private int currentBufferIndex;
    /**
     * The total count of bytes in all the filled buffers.
     */
    private int filledBufferSum;
    /**
     * The current buffer.
     */
    private byte[] currentBuffer;
    /**
     * The total count of bytes written.
     */
    private int count;


    public ByteArrayOutputStream() {
        this(1024);
    }


    public ByteArrayOutputStream(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(
                    "Negative initial size: " + size);
        }
        synchronized (this) {
            needNewBuffer(size);
        }
    }

    private void needNewBuffer(int newcount) {
        if (currentBufferIndex < buffers.size() - 1) {
            //Recycling old buffer
            filledBufferSum += currentBuffer.length;

            currentBufferIndex++;
            currentBuffer = buffers.get(currentBufferIndex);
        } else {
            //Creating new buffer
            int newBufferSize;
            if (currentBuffer == null) {
                newBufferSize = newcount;
                filledBufferSum = 0;
            } else {
                newBufferSize = Math.max(
                        currentBuffer.length << 1,
                        newcount - filledBufferSum);
                filledBufferSum += currentBuffer.length;
            }

            currentBufferIndex++;
            currentBuffer = new byte[newBufferSize];
            buffers.add(currentBuffer);
        }
    }


    @Override
    public void write(byte[] b, int off, int len) {

    }


    public synchronized int write(InputStream in) throws IOException {
        int readCount = 0;
        int inBufferPos = count - filledBufferSum;
        int n = in.read(currentBuffer, inBufferPos, currentBuffer.length - inBufferPos);
        while (n != -1) {
            readCount += n;
            inBufferPos += n;
            count += n;
            if (inBufferPos == currentBuffer.length) {
                needNewBuffer(currentBuffer.length);
                inBufferPos = 0;
            }
            n = in.read(currentBuffer, inBufferPos, currentBuffer.length - inBufferPos);
        }
        return readCount;
    }


    public synchronized int size() {
        return count;
    }


    @Override
    public void close() throws IOException {
        //nop
    }


    public InputStream toInputStream() {
        int remaining = count;
        if (remaining == 0) {
            return new ClosedInputStream();
        }
        List<ByteArrayInputStream> list = new ArrayList<ByteArrayInputStream>(buffers.size());
        for (byte[] buf : buffers) {
            int c = Math.min(buf.length, remaining);
            list.add(new ByteArrayInputStream(buf, 0, c));
            remaining -= c;
            if (remaining == 0) {
                break;
            }
        }
        return new SequenceInputStream(Collections.enumeration(list));
    }


    @Override
    public String toString() {
        return new String();
    }


    public String toString(String enc) throws UnsupportedEncodingException {
        return new String();
    }

}
