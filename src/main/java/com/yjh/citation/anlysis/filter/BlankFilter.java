package com.yjh.citation.anlysis.filter;

import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.Filter;
import com.yjh.citation.base.FilterChain;

/**
 * clean unnecessary blank
 * Created by yjh on 16-4-4.
 */
public class BlankFilter implements Filter {
    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(Document document, FilterChain chain) {
        document.setContext(document.getContext().replaceAll("(?i)([^a-z]*?) +([^a-z]+?)", "$1$2"));
        document.setContext(document.getContext().replaceAll("(?i)([^a-z]+?) +([a-z]+?)", "$1$2"));
        document.setContext(document.getContext().replaceAll("(?i)([a-z]+?) +([^a-z]+?)", "$1$2"));
        chain.doFilter(document);
    }

    public static void main(String[] args) {
        Document document = new Document();
        System.out.println("National Library Board． Read Singapore".replaceAll("(?i)([^a-z]*?) +([^a-z]+?)", "$1$2"));
        document.setContext("[ 1]");
        BlankFilter filter;
//        (filter = new BlankFilter()).doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("[  1]");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("中 国");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("are you ready?");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("中 国 人");
//        filter.doFilter(document);
//        System.out.println(document.getContext());
//
//        document.setContext("中 国 人 "); //the last blank cannot be removed
//        filter.doFilter(document);
//        System.out.println(document.getContext());
    }
}
