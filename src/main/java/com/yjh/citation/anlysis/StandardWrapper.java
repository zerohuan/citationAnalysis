package com.yjh.citation.anlysis;

import com.yjh.citation.anlysis.filter.*;
import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.FilterChain;
import com.yjh.citation.base.container.AbstractContainer;
import com.yjh.citation.base.container.Wrapper;
import com.yjh.citation.base.model.DataWrapper;
import com.yjh.citation.base.model.Document;
import com.yjh.io.M2TxtLogger;
import com.yjh.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by yjh on 16-4-13.
 */
public class StandardWrapper extends AbstractContainer implements Wrapper {
    private FilterChain chain;
    private final M2TxtLogger logger = new M2TxtLogger();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void destroy() {

    }

    @Override
    public void init(AnalysisContext context) {
        super.init(context);
        chain = new FilterChain(context);
        chain.addFilter(new DirtyCharFilter());
        chain.addFilter(new BlankFilter());
        chain.addFilter(new SymbolFilter());
        chain.addFilter(new ReferenceContextFilter());
        chain.addFilter(new ReferenceCleanFilter());
        chain.addFilter(new ReferenceExtractFilter());
        chain.addFilter(new ReferenceCountingFilter());
        chain.addFilter(new ParticipleFilter());
    }

    @Override
    public void start(List<Document> documents) {
        int i = 0;
        for (Document document : documents) {
            chain.reset();
            System.out.println(++i + "##" + document.getTitle() + "##");
            chain.doFilter(document);
            System.out.println(context);

            logger.log("##标题##" + document.getTitle());
            logger.log(document.printTokens() + "\r\n");
//            System.out.println(document.getReferenceContent());
            System.out.println(document.printTokens());
            System.out.println();
        }
        try {
            logger.flush("/home/yjh/data/log_tokens.txt", false, "gbk");
            FileUtil.serialize2File(BootStrap.dataPath, new DataWrapper(documents), "utf8");
        } catch (IOException e) {
            LOGGER.error(e);
        }
//        startAllChildren(documents);
    }
}
