package com.citation.anlysis.filter;

import com.citation.base.model.Document;
import com.citation.base.Filter;
import com.citation.base.FilterChain;
import com.citation.base.model.Reference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 16-4-8.
 */
public class ReferenceCountingFilter implements Filter {
    private static final Pattern PATTERN = Pattern.compile("\\[([\\d~\\-,]+?\\])");

    @Override
    public void doFilter(Document document, FilterChain chain) {
        Matcher matcher = PATTERN.matcher(document.getContext());
        while (matcher.find()) {
            handleRefer(document, matcher.group(1));
        }
//        for (Reference r : document.getReferenceList())
//            System.out.println(r);
        chain.doFilter(document);
    }

    private void handleRefer(Document document, String content) {
        String[] fms = content.split(",");
        for (String fm : fms) {
            //TODO Maybe interceptor model is better
            if (fm.matches("\\d+")) {
                addCount(document, Integer.parseInt(fm));
            } else if (fm.matches("\\d+[~\\-]\\d+")) {
                String[] ns = fm.split("~|\\-");
                addCount(document, Integer.parseInt(ns[0]), Integer.parseInt(ns[1]));
            }
        }
    }

    private void addCount(Document document, int n) {
        for (Reference r : document.getReferenceList()) {
            if (r.getNumber() == n)
                r.setCount(r.getCount() + 1);
        }
    }

    private void addCount(Document document, int start, int end) {
        for (Reference r : document.getReferenceList()) {
            if (r.getNumber() >= start && r.getNumber() <= end)
                r.setCount(r.getCount() + 1);
        }
    }

    @Override
    public void init() {

    }

    public static void main(String[] args) {
        Filter filter = new ReferenceCountingFilter();
        Document document = new Document();
        document.setContext("[1][2]");

    }
}
