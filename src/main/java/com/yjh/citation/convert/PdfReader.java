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
	 * ת������pdf�ļ�
	 * @param file
	 * @param outPath
	 * @param encoding
	 * @throws Exception
	 */
    public String readFdf(File file, String outPath, String encoding)
    {
        // �Ƿ�����   
        boolean sort = false;
        // pdf�ļ���   
        File pdfFile = file;
        // �����ı��ļ�����   
        File textFile = null;
        // ��ʼ��ȡҳ��   
        int startPage = 1;
        // ������ȡҳ��   
        int endPage = Integer.MAX_VALUE;
        // �ļ��������������ı��ļ�       
        Writer output = null;
        // �ڴ��д洢��PDF Document   
        PDDocument document = null;
        // ��ȡPDF���ļ���  
        String fileName = file.getName();
        try
        {
            //ע������Ѳ�����ǰ�汾�е�URL.����File��   
            document = PDDocument.load(pdfFile);
            // ��ԭ��PDF�������������²�����txt�ļ�   
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
            // �ļ���������д���ļ���textFile   
            output = new PrintWriter(textFile, encoding);
            // PDFTextStripper����ȡ�ı�   
            PDFTextStripper stripper = null;
            stripper = new PDFTextStripper();
            // �����Ƿ�����   
            stripper.setSortByPosition(sort);
            // ������ʼҳ   
            stripper.setStartPage(startPage);
            // ���ý���ҳ   
            stripper.setEndPage(endPage);
            // ����PDFTextStripper��writeText��ȡ������ı�
            stripper.writeText(document, output);
        } catch(Exception e) {
        	fileName = null;
        } finally{
            if (output != null)
            {
                // �ر������   
                try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            if (document != null)
            {
                // �ر�PDF Document   
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
