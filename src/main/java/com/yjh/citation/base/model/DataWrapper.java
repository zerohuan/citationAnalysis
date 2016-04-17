package com.yjh.citation.base.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yjh on 16-4-17.
 */
public class DataWrapper implements Serializable {
    private List<Document> documents;

    public DataWrapper() {
    }

    public DataWrapper(List<Document> documents) {
        this.documents = documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
