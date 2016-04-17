package com.yjh.citation.anlysis.generator;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Token;

import java.util.List;

/**
 * Created by yjh on 16-4-17.
 */
public interface Generator {
    String RELATION_SHIP_SIG = "@relation contact-lenses";
    String  ATTRIBUTE_SIG = "@attribute";
    String DATA_SIG = "@data";

    void generateAttribute(StringBuilder buffer, List<Document> documents, AnalysisContext context);
    void generateData(StringBuilder buffer, List<Document> documents, AnalysisContext context);
    double computeValue(Token token, AnalysisContext context);
}
