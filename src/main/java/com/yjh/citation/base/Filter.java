package com.yjh.citation.base;

import com.yjh.citation.base.model.Document;

/**
 * handler of text content
 * Created by yjh on 16-4-4.
 */
public interface Filter {
    void init();
    void doFilter(Document document, com.yjh.citation.base.FilterChain chain);
    void destroy();
}
