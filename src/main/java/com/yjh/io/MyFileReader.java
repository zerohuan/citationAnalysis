package com.yjh.io;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

/**
 * ʹ���ڴ�ӳ���ļ���Ϊ
 * @author yjh
 *
 */
public class MyFileReader {
	private FileChannel fc;
	private FileInputStream fi;
	private String encoding;
	public MyFileReader(String filename, String encoding) {
		super();
		try {
			this.fi= new FileInputStream(filename);
			this.fc = fi.getChannel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.encoding = encoding;
	}
	private void close() {
		try {
			this.fc.close();
			this.fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getContent() {
		String content = "";
		try {
			//ʹ���ڴ�ӳ���ڹܵ����ȡ
			MappedByteBuffer mb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fi.available());
			//ָ���ַ����
			content = Charset.forName(encoding).decode(mb).toString();
			this.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
}
