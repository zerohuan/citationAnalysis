package com.yjh.citation.base.container;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;

import java.util.List;

/**
 * Created by yjh on 16-4-13.
 */
public interface Container {
    void init(AnalysisContext context);
    void start(List<Document> documents);
    void destroy();
    String getName();
    void setName(String name);
    void setParent(Container parent);
    void addChild(Container child);
    Container findChild(String name);
    Container[] findChildren();
}
