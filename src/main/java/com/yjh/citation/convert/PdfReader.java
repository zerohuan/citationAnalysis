package com.yjh.citation.convert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class PdfReader
{	
	/**
	 * 转换单个pdf文件
	 * @param file
	 * @param outPath
	 * @param encoding
	 * @throws Exception
	 */
    public String readFdf(File file, String outPath, String encoding)
    {
        // 是否排序   
        boolean sort = false;
        // pdf文件名   
        File pdfFile = file;
        // 输入文本文件名称   
        File textFile = null;
        // 开始提取页数   
        int startPage = 1;
        // 结束提取页数   
        int endPage = Integer.MAX_VALUE;
        // 文件输入流，生成文本文件       
        Writer output = null;
        // 内存中存储的PDF Document   
        PDDocument document = null;
        // 获取PDF的文件名  
        String fileName = file.getName();
        try
        {
            //注意参数已不是以前版本中的URL.而是File。   
            document = PDDocument.load(pdfFile);
            // 以原来PDF的名称来命名新产生的txt文件   
            if (fileName.length() > 4)
            {
                File outputFile = new File(outPath + fileName.substring(0, fileName.length() - 4)
                        + ".txt");
                textFile = outputFile;
            } else
                textFile = new File(outPath + System.currentTimeMillis()
                        + ".txt");
            if (!textFile.exists()) {
                textFile.getParentFile().mkdirs();
                textFile.createNewFile();
            }
            // 文件输入流，写入文件倒textFile   
            output = new PrintWriter(textFile, encoding);
            // PDFTextStripper来提取文本   
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // 设置是否排序   
            stripper.setSortByPosition(sort);
            // 设置起始页   
            stripper.setStartPage(startPage);
            // 设置结束页   
            stripper.setEndPage(endPage);
            // 调用PDFTextStripper的writeText提取并输出文本
            stripper.writeText(document, output);
        } catch(Exception e) {
        	fileName = null;
        } finally{
            if (output != null)
            {
                // 关闭输出流   
                try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            if (document != null)
            {
                // 关闭PDF Document   
                try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        
        return textFile != null ? textFile.getName() : "";
    }
    
}
