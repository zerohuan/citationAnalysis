package com.citation.base;

import com.citation.base.model.Document;
import com.citation.base.resource.Resource;

import java.util.List;

/**
 * The context info in whole analysis
 * Created by yjh on 16-4-8.
 */
public interface AnalysisContext {
    Resource getResource(String contextPath, String encoding, String metaPath);
    int getBadDocCount();
    int addBadDocCount(Document document);
    int getHandledDocCount();
    int addHandledDocCount(Document document);
    List<Document> getBadDocs();
}
