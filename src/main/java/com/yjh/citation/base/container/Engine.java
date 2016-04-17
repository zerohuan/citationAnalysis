package com.yjh.citation.base.container;

import com.yjh.citation.base.resource.DocumentReader;
import com.yjh.citation.base.resource.Resource;

/**
 * Created by yjh on 16-4-13.
 */
public interface Engine extends Container {
    public DocumentReader getReader();
    public void setReader(DocumentReader reader);
    public Resource getResource();
    public void setResource(Resource resource);
}
