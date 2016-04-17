package com.yjh.citation.base;

import com.yjh.citation.base.model.Document;

/**
 * thread unsafe
 * Created by yjh on 16-4-4.
 */
public class FilterChain {
    private static final int INCREMENT = 10;

    private Filter[] filters = {};
    private int n = 0;
    private int pos = 0;
    private final AnalysisContext context;

    public FilterChain(AnalysisContext context) {
        this.context = context;
    }

    public AnalysisContext getContext() {
        return context;
    }

    public void addFilter(Filter filter) {
        for(Filter f : filters)
            if(f == filter)
                return;

        if (n == filters.length) {
            Filter[] newFilters =
                    new Filter[n + INCREMENT];
            System.arraycopy(filters, 0, newFilters, 0, n);
            filters = newFilters;
        }
        filters[n++] = filter;
        filter.init();
    }

    public void doFilter(Document document) {
        if (pos < n) {
            Filter filter = filters[pos++];
            filter.doFilter(document, this);
        }
    }

    public void reset() {
        pos = 0;
    }

}
