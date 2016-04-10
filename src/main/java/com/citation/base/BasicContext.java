package com.citation.base;

import com.citation.base.model.Document;
import com.citation.base.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yjh on 16-4-8.
 */
public class BasicContext implements AnalysisContext {
    private int badDocCount;
    private int handledCount;
    private Resource rootResource;
    private List<Document> badDocs;

    @Override
    public int getBadDocCount() {
        return badDocCount;
    }

    @Override
    public int addBadDocCount(Document document) {
        System.out.println(document);
        if (badDocs == null)
            badDocs = new ArrayList<>();
        badDocs.add(document);
        return ++badDocCount;
    }

    @Override
    public Resource getResource(String contextPath, String encoding, String metaPath) {
        return rootResource = new Resource(contextPath, encoding, metaPath);
    }

    @Override
    public int addHandledDocCount(Document document) {
        return ++handledCount;
    }

    @Override
    public int getHandledDocCount() {
        return handledCount;
    }

    @Override
    public List<Document> getBadDocs() {
        return badDocs;
    }

    @Override
    public String toString() {
        return "BasicContext{" +
                "badDocCount=" + badDocCount +
                ", handledCount=" + handledCount +
                '}';
    }
}
