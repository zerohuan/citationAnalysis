package com.yjh.citation.anlysis.generator;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Token;

import java.util.List;

/**
 * Created by yjh on 16-4-17.
 */
public class TokenAllMatrixGenerator implements Generator {
    @Override
    public void generateAttribute(StringBuilder buffer, List<Document> documents, AnalysisContext context) {
        for (String word : context.getTokenMap().keySet())
            buffer.append(ATTRIBUTE_SIG).append(" ").append(word).append(" numeric").append("\r\n");
    }

    @Override
    public void generateData(StringBuilder buffer, List<Document> documents, AnalysisContext context) {
        for (Document document : documents) {
            String[] words = new String[context.getTokenMap().size()];
            context.getTokenMap().keySet().toArray(words);
            for (int i = 0; i < words.length; ++i ) {
                String word = words[i];
                Token token;
                if ((token = document.hasToken(word)) == null)
                    buffer.append("0");
                else
                    buffer.append(token.getValue());
                buffer.append(",");
            }
            buffer.append(document.getTheme());
            buffer.append("\r\n");
        }
    }

    @Override
    public double computeValue(Token token, AnalysisContext context) {
        int[] tfs = token.getTfs();
        int[] weights = context.getWeights();
        return tfs[0] * weights[0] + tfs[1] * weights[1] + tfs[2] * weights[2] + tfs[3] * weights[3];
    }
}
