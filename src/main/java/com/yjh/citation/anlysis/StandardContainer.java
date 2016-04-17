package com.yjh.citation.anlysis;

import com.yjh.citation.base.container.AbstractContainer;
import com.yjh.citation.base.model.DataWrapper;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Reference;
import com.yjh.citation.base.model.Token;
import com.yjh.io.FileAccessor;
import com.yjh.util.JsonOperator;
import com.yjh.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Created by yjh on 16-4-17.
 */
public class StandardContainer extends AbstractContainer {
    private static final Logger logger = LogManager.getLogger();
    private String dataPath;
    private String encoding;

    public StandardContainer(String dataPath, String encoding) {
        this.dataPath = dataPath;
        this.encoding = encoding;
    }

    @Override
    public void start(List<Document> documents) {
        ObjectMapper mapper = new JsonOperator().getJsonMapper();
        try {
            String data = FileAccessor.readAll(dataPath, encoding);
            DataWrapper dataWrapper = mapper.readValue(data.getBytes(), DataWrapper.class);
            documents = dataWrapper.getDocuments();
            boolean[] isPartHasOrNot = context.isPartHasOrNot();
            for (Document document : documents) {
                //references
                for (Reference reference : document.getReferenceList()) {
                    context.addTitle(StringUtil.standardTitle(reference.getTitle()));
                }

                //tokens
                for (Token token : document.getTokens().values()) {
                    for (int i = 0; i < isPartHasOrNot.length; ++i) {
                        if (isPartHasOrNot[i] && token.getTfs()[i] > 0) {
                            context.addToken(token.getWord());
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }

        startAllChildren(documents);
    }

    public static void main(String[] args) {
        StandardContainer container = new StandardContainer("/home/yjh/data/documents.txt", "utf8");
        container.start(null);
    }
}
