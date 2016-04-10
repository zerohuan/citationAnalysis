package com.citation.anlysis.filter;

import com.citation.base.model.Document;
import com.citation.base.Filter;
import com.citation.base.FilterChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler symbol:
 * convert some chinese symbol to appropriate english one
 * convert full-width symbol to appropriate half-width one
 * Created by yjh on 16-4-4.
 */
public class SymbolFilter implements Filter {
    private Logger logger = LogManager.getLogger(SymbolFilter.class);
    private Pattern pattern;

    private List<SymbolItem> badSymbolPairs = new ArrayList<>();

    @Override
    public void init() {
        badSymbolPairs.add(new SymbolItem("【", "】"));
        badSymbolPairs.add(new SymbolItem("〔", "〕"));

        StringBuilder buffer = new StringBuilder("[\u00A4-\u2160]|[\uFF01-\uFFE5]");
        for (SymbolItem item : badSymbolPairs) {
            buffer.append("|(").append(item.getLeft()).append(")");
            buffer.append("|(").append(item.getRight()).append(")");
        }
        pattern = Pattern.compile(buffer.toString());
    }

    @Override
    public void doFilter(Document document, FilterChain chain) {
        Matcher matcher = pattern.matcher(document.getContext());
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            try {
                int groupCount = matcher.groupCount();
                boolean flag = false;
                for (int i = 0; i < groupCount; ++i) {
                    if (matcher.group(i + 1) != null) {
                        if ((i & 1) == 0)
                            matcher.appendReplacement(buffer, "[");
                        else
                            matcher.appendReplacement(buffer, "]");
                        flag = true;
                    }
                }
                if (!flag) {
                    String replacement = full2HalfChange(matcher.group());
                    if (replacement.equals("$") || replacement.equals("\\"))
                        replacement = "\\" + replacement;
                    matcher.appendReplacement(buffer, replacement);
                }
            } catch(Exception e) {
                e.printStackTrace();
                logger.debug("***************reference symbol error: " + matcher.group() + " "
                        + full2HalfChange(matcher.group()) +"*********************");
            }
        }
        document.setContext(matcher.appendTail(buffer).toString());
        chain.doFilter(document);
    }

    /**
     * full-width to half-width
     * @param QJstr
     * @return
     */
    public static final String full2HalfChange(String QJstr)  {
        StringBuilder outStrBuf = new StringBuilder();
        String Tstr = "";
        byte[] b = null ;

        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            // f-w blank
            if (Tstr.equals("　")) {
                outStrBuf.append(" ");
                continue;
            }
            try {
                b = Tstr.getBytes( "unicode");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (b[2] == -1) {
                // f-w ？
                b[3] = ( byte) (b[3] + 32);
                b[2] = 0;
                try {
                    outStrBuf.append(new String(b, "unicode" ));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                outStrBuf.append(Tstr);
            }
        } // end for.
        return outStrBuf.toString();
    }


    //unit test
    public static void main(String[] args) {
        Document document = new Document();


        SymbolFilter filter;
        filter = new SymbolFilter();

        filter.init();
        System.out.println(full2HalfChange("＄"));
//        document.setContext("【");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("】");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("【1】中国中国");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("＋－＊／＝？");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
    }
}
