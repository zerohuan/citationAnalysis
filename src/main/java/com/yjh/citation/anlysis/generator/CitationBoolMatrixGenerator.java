package com.yjh.citation.anlysis.generator;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Reference;
import com.yjh.citation.base.model.Token;
import com.yjh.util.StringUtil;

import java.util.List;

/**
 * Created by yjh on 16-4-17.
 */
public class CitationBoolMatrixGenerator implements Generator {
    @Override
    public void generateAttribute(StringBuilder buffer, List<Document> documents, AnalysisContext context) {
        for (String title : context.getTitleMap().keySet())
            buffer.append(Generator.ATTRIBUTE_SIG).append(" ").append(title).append(" numeric").append("\r\n");
    }

    @Override
    public void generateData(StringBuilder buffer, List<Document> documents, AnalysisContext context) {
        for (Document document : documents) {
            int[] titles = new int[context.getTitleMap().size()];
            for (Reference reference : document.getReferenceList()) {
                String title = StringUtil.standardTitle(reference.getTitle());
                Integer id;
                if ((id = context.getTitleMap().get(title)) != null)
                    titles[id] = reference.getCount();
            }
            for (int i = 0; i < titles.length; ++i ) {
                buffer.append(titles[i]);
                buffer.append(",");
            }
            buffer.append(document.getTheme());
            buffer.append("\r\n");
        }
    }

    @Override
    public double computeValue(Token token, AnalysisContext context) {
        int[] tfs = token.getTfs();
        return tfs[3];
    }
}
