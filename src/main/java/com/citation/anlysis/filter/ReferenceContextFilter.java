package com.citation.anlysis.filter;

import com.citation.base.model.Document;
import com.citation.base.Filter;
import com.citation.base.FilterChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 16-4-6.
 */
public class ReferenceContextFilter implements Filter {
    private Logger logger = LogManager.getLogger(ReferenceContextFilter.class);

    private final Pattern pattern = Pattern.compile("(?s)(.+?)\n\\[?参考文献[:：]?.{0,5}\n(.+)");
    private static final String SYMBOL_UNION = "[*|?|？|─|&|、|/|\\\\|:|\\-|\"|'|\\||《|》|“|”|‘|’|！|!|~|(|)]+";

    @Override
    public void doFilter(Document document, FilterChain chain) {
        Matcher matcher = pattern.matcher(document.getContext());
        if (matcher.find()) {
            document.setContext(matcher.group(1));
            String referenceContent = matcher.group(2);
            if (referenceContent.startsWith("]"))
                referenceContent = referenceContent.substring(1, referenceContent.length());
            //替换掉其他的标点符号
            referenceContent = referenceContent.replaceAll("(?i)([a-z]+?)-\r\n", "$1");
            referenceContent = referenceContent.replaceAll(SYMBOL_UNION, "_");
            referenceContent = referenceContent
                    .replaceAll("([一-龥]{2,4},)+译\\.","")
                    .replace("·",",")
                    .replace(",等", "");
            document.setTitle(document.getTitle().replaceAll(SYMBOL_UNION, "_"));
            document.setReferenceContent(referenceContent);
        }
        chain.doFilter(document);
    }

    @Override
    public void init() {

    }

    public static void main(String[] args) {
        ReferenceContextFilter filter = new ReferenceContextFilter();

    }
}
