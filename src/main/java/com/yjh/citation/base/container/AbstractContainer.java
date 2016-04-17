package com.yjh.citation.base.container;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yjh on 16-4-15.
 */
public abstract class AbstractContainer implements Container {
    protected Map<String, Container> children = new HashMap<>();
    private String name;
    protected Container parent;
    protected AnalysisContext context;

    @Override
    public void init(AnalysisContext context) {
        this.context = context;
        for (Container container : findChildren())
            container.init(context);
    }

    @Override
    public void destroy() {
        if (children != null)
            children.clear();
    }

    protected void startAllChildren(List<Document> documents) {
        for (Container child : findChildren())
            child.start(documents);
    }

    @Override
    public Container findChild(String name) {
        if (name == null)
            return null;
        synchronized (children) {
            return children.get(name);
        }
    }

    @Override
    public Container[] findChildren() {
        synchronized (children) {
            //TODO why not toArray() directly, but toArray([])
            Container[] containers = new Container[children.size()];
            return children.values().toArray(containers);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addChild(Container child) {
        synchronized(children) {
            if (children.get(child.getName()) != null)
                throw new IllegalArgumentException("addChild:  Child name '" +
                        child.getName() +
                        "' is not unique");
            child.setParent(this);  // May throw IAE
            children.put(child.getName(), child);
        }
    }

    @Override
    public void setParent(Container parent) {
        this.parent = parent;
    }
}
