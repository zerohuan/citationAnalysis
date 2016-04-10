package com.citation.base;

import com.citation.base.model.Document;

/**
 * handler of text content
 * Created by yjh on 16-4-4.
 */
public interface Filter {
    void init();
    void doFilter(Document document, com.citation.base.FilterChain chain);
}
