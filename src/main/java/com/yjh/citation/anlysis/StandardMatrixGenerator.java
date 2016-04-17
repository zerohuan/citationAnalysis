package com.yjh.citation.anlysis;

import com.yjh.citation.anlysis.generator.Generator;
import com.yjh.citation.base.container.AbstractContainer;
import com.yjh.citation.base.container.MatrixGenerator;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Token;
import com.yjh.io.FileAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yjh on 16-4-15.
 */
public class StandardMatrixGenerator extends AbstractContainer implements MatrixGenerator {
    private static final Logger logger = LogManager.getLogger();

    private final StringBuilder buffer = new StringBuilder();
    private String outPath;
    private String encoding;
    private Generator generator;

    public StandardMatrixGenerator(String outPath, String encoding, Generator generator) {
        this.encoding = encoding;
        this.outPath = outPath;
        this.generator = generator;
    }

    @Override
    public void start(List<Document> documents) {
        System.out.println(context);
        System.out.println(context.getTokenMap());

        for (Document document : documents) {
            for (Iterator<Token> itr = document.getTokens().values().iterator(); itr.hasNext();) {
                Token token = itr.next();
                String word = token.getWord();
                int dtf = context.getTokenDF(word);
                if (dtf < 5 || dtf > 600) {
                    context.getTokenMap().remove(word);
                    itr.remove();
                } else {
                    token.setValue(generator.computeValue(token, context));
                }
            }
        }

        buffer.append(Generator.RELATION_SHIP_SIG).append("\r\n");

        generator.generateAttribute(buffer, documents, context);

        buffer.append("@attribute Class {0,1,2,3,4,5}\r\n");
        buffer.append(Generator.DATA_SIG).append("\r\n");

        generator.generateData(buffer, documents, context);
        try {
            FileAccessor.writeAll(outPath, buffer.toString(), encoding);
        } catch (IOException e) {
            logger.error(e);
        }
        startAllChildren(documents);
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getOutPath() {
        return outPath;
    }

    @Override
    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }
}
