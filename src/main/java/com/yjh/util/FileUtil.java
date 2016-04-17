package com.yjh.util;

import com.yjh.io.FileAccessor;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by yjh on 15-12-8.
 */
public class FileUtil {
    public static File getFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    public static void serialize2File(String path, Object obj, String encoding) throws IOException {
        ObjectMapper mapper = new JsonOperator().getJsonMapper();
        String str = mapper.writeValueAsString(obj);
        FileAccessor.writeAll(path, str, encoding);
    }
}
