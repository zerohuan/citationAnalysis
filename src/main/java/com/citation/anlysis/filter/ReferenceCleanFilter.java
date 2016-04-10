package com.citation.anlysis.filter;

import com.citation.base.model.Document;
import com.citation.base.Filter;
import com.citation.base.FilterChain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 专门处理参考文献部分，将每条参考文献单独一行
 * Created by yjh on 16-4-7.
 */
public class ReferenceCleanFilter implements Filter {
    //chinese reference head
    protected static final String PATTERN_CN = "^\\[?\\d{1,3}(\\]|\\.)?([\u4E00-\u9FA5]{2,4}[,\\.])+?(.*)";
    //network
    protected static final String PATTERN_NET = "^\\[?\\d{1,3}(\\]|\\.)?([a-zA-Z\u4E00-\u9FA5_,]{4,})+?\\.?\\[(EB|\\d{4})(.*)";
    //english reference head
    protected static final String PATTERN_ENG = "(?i)^\\[?\\d{1,3}\\]?([a-z ]{2,}[,\\.]?)+?(.*)";
    private int i;
    @Override
    public void doFilter(Document document, FilterChain chain) {
        StringBuilder buffer = new StringBuilder();
        if (document.getReferenceContent() != null)
            try (BufferedReader reader =
                         new BufferedReader(new StringReader(document.getReferenceContent()))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.matches("^.?(参考文献|注释)(.*)"))
                        break;
                    if (i > 0 &&
                            (line.matches(PATTERN_CN) ||
                                    line.matches(PATTERN_ENG) ||
                                    line .matches(PATTERN_NET)))
                        buffer.append("\r\n");
                    line = line.trim();
                    if (!line.equals("")) {
                        buffer.append(line);
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        document.setReferenceContent(buffer.toString());
        chain.doFilter(document);
    }

    @Override
    public void init() {

    }
}
