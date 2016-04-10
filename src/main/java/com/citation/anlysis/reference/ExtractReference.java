package com.citation.anlysis.reference;

import com.citation.anlysis.filter.*;
import com.citation.base.AnalysisContext;
import com.citation.base.BasicContext;
import com.citation.base.FilterChain;
import com.citation.base.io.M2TxtLogger;
import com.citation.base.model.Document;
import com.citation.base.model.Reference;
import com.citation.base.resource.DocumentReader;
import com.citation.base.resource.MetaRule;

import java.util.List;

/**
 * Created by yjh on 16-4-7.
 */
public class ExtractReference {
    AnalysisContext context;
    private FilterChain chain;
    private M2TxtLogger logger = new M2TxtLogger();

    {
        context = new BasicContext();
        chain = new FilterChain(context);
        chain.addFilter(new DirtyCharFilter());
        chain.addFilter(new BlankFilter());
        chain.addFilter(new SymbolFilter());
        chain.addFilter(new ReferenceContextFilter());
        chain.addFilter(new ReferenceCleanFilter());
        chain.addFilter(new ReferenceExtractFilter());
        chain.addFilter(new ReferenceCountingFilter());
    }

    public void doExtract() throws Exception {
        //step 1: generating Documents from txt
        DocumentReader reader = new DocumentReader(true, ".txt",
                new MetaRule.AuthorCNV(),
                new MetaRule.AuthorEng(),
                new MetaRule.Category(),
                new MetaRule.Institution(),
                new MetaRule.Keywords(),
                new MetaRule.Abstract());
        List<Document> documents =  reader.load(
                context.getResource(
                        "/home/yjh/data/txt",
                        "gbk",
                        "/home/yjh/data/base"
                ));

        //step 2: extract reference in document
        int i = 0;
        for (Document document : documents) {
            chain.reset();
            System.out.println(++i + "##" + document.getTitle() + "##");
            chain.doFilter(document);
            System.out.println(context);

//            logger.log("##БъЬт##" + document.getTitle());
//            logger.log(document.getReferenceContent() + "\r\n");
//            System.out.println(document.getReferenceContent());
//            for (Reference f : document.getReferenceList())
//                System.out.println(f);
            System.out.println();
        }
        if (context.getBadDocs() != null)
            for (Document document : context.getBadDocs()) {
                System.out.println(++i + "##" + document.getTitle() + "##");
                System.out.println(document.getReferenceContent());
                for (Reference f : document.getReferenceList())
                    System.out.println(f);
                System.out.println();
            }
//        logger.flush("/home/yjh/data/log.txt", false, "gbk");
    }

    public static void main(String[] args) throws Exception {
        ExtractReference extractReference = new ExtractReference();
        extractReference.doExtract();
    }
}
