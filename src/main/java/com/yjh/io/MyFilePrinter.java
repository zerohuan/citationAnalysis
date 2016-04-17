package com.yjh.io;

import java.io.*;

public class MyFilePrinter implements Closeable {
	private PrintWriter out;
	private OutputStreamWriter ops;
	private FileOutputStream fos;
	public MyFilePrinter(String filename, boolean append, String encoding) {
		try {
			File file = new File(filename);
			if(!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			fos = new FileOutputStream(filename, append);
			ops = new OutputStreamWriter(fos, encoding);
			out = new PrintWriter(ops);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		this.out.close();
		try {
			this.ops.close();
			this.fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void write(String content) {
		out.write(content);
	}
}
