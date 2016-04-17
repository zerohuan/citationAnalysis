package com.yjh.citation.base.resource;

/**
 * Created by yjh on 16-4-7.
 */
public class Resource {
    private String contextPath;
    private String metaPath;
    private String encoding;

    public Resource(String contextPath, String encoding, String metaPath) {
        this.contextPath = contextPath;
        this.encoding = encoding;
        this.metaPath = metaPath;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getMetaPath() {
        return metaPath;
    }

    public void setMetaPath(String metaPath) {
        this.metaPath = metaPath;
    }
}
