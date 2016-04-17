package com.yjh.io;

import java.io.IOException;

/**
 * Created by yjh on 16-4-8.
 */
public class M2TxtLogger {
    private StringBuilder buffer = new StringBuilder();

    public void log(String msg) {
        System.out.println(msg);
        buffer.append(msg).append("\r\n");
    }

    public void flush(String path, boolean append, String encoding) throws IOException {
        if (append)
            FileAccessor.writeAppend(path, buffer.toString(), encoding);
        else
            FileAccessor.writeAll(path, buffer.toString(), encoding);
        buffer.setLength(0);
    }
}
