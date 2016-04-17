package com.yjh.citation.anlysis;

import com.yjh.citation.base.container.AbstractContainer;
import com.yjh.citation.base.container.Engine;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.resource.DocumentReader;
import com.yjh.citation.base.resource.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by yjh on 16-4-13.
 */
public class StandardEngine extends AbstractContainer implements Engine {
    private static final Logger logger = LogManager.getLogger();
    private DocumentReader reader;
    private Resource resource;

    @Override
    public void destroy() {

    }

    @Override
    public void start(List<Document> documents) {
        if (reader == null || resource == null)
            throw new IllegalArgumentException("reader and resource cannot be null");
        try {
            documents =  reader.load(
                    resource);
            startAllChildren(documents);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public DocumentReader getReader() {
        return reader;
    }

    public void setReader(DocumentReader reader) {
        this.reader = reader;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
