package com.yjh.tool;

import com.yjh.util.FileUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by yjh on 15-12-8.
 */
public class CardNumber {

    private static final char[] chars = {'1','2','3','4','5','6','7','8','9'};
    private static final String FileName = "/home/yjh/dms/tmp/card_number.txt";
    @Test
    public void genericCardNumber() throws IOException {
        int endNumber = 200000;
        StringBuilder sb = new StringBuilder();
        StringBuilder tempSb = new StringBuilder();
        String preFix = "6818000012";
        for(int i = 100000; i < endNumber; ++i) {
            sb.append(preFix);
            tempSb.setLength(0);
            int tempI = i;
            while(tempI > 0) {
                int temp = tempI % 9;
                tempSb.insert(0,chars[temp]);
                tempI /= 9;
            }
            sb.append(tempSb);
            if(i < endNumber - 1) {
                sb.append('\n');
            }
        }
        RandomAccessFile fos = new RandomAccessFile(FileUtil.getFile(FileName), "rw");
        MappedByteBuffer mappedByteBuffer = fos.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, sb.length());
        mappedByteBuffer.put(sb.toString().getBytes());
        fos.close();
    }
}
