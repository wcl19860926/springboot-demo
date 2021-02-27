package com.study.io;

import java.io.InputStream;

public class ClosedInputStream extends InputStream {

    /**
     * A singleton.
     */
    public static final org.apache.commons.io.input.ClosedInputStream CLOSED_INPUT_STREAM = new org.apache.commons.io.input.ClosedInputStream();

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() {
        return -1;
    }

}