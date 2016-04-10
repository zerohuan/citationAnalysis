package com.citation.convert;

import java.io.File;

/**
 *
 * Created by yjh on 2016/3/31.
 */
public class Pdf2Txt {
    private String encoding;
    private String outputPath;
    private PdfReader executor = new PdfReader();
    private StringBuilder builder = new StringBuilder();

    public Pdf2Txt(String encoding, String outputPath) {
        this.encoding = encoding;
        this.outputPath = outputPath;
        File output = new File(outputPath);
        if (!output.exists()) {
            output.mkdirs();
        }
    }

    public void convert(File pdfDir) {
        convert(pdfDir, "");
    }

    private void convert(File pdfDir, String dir) {
        if(pdfDir.isDirectory()) {
            File[] docs = pdfDir.listFiles();
            if (docs != null) {
                for(File f : docs) {
                    convert(f, pdfDir.getName());
                }
            }
        } else {
            if(pdfDir.getName().matches(".*\\.pdf")) {
                try {
                    builder.setLength(0);
                    String path;
                    String filename = executor.readFdf(pdfDir,
                            path = builder.append(outputPath).append("/").append(dir).append("/").toString(), encoding);
                    System.out.println(path + filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Pdf2Txt("GBK", "/home/yjh/data/txt²¹/").convert(new File
                ("/home/yjh/data/²¹")); //original_pdf
    }
}
