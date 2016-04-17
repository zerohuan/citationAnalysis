package com.yjh.citation.anlysis;

import com.yjh.citation.base.container.AbstractContainer;
import com.yjh.citation.base.container.MatrixHandler;
import com.yjh.citation.base.model.Document;
import com.yjh.io.MyFilePrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yjh on 16-4-15.
 */
public class StandardMatrixHandler extends AbstractContainer implements MatrixHandler {
    private static final Logger logger = LogManager.getLogger();
    private String outPath;
    private String resultPath;
    private String encoding;

    public StandardMatrixHandler(String outPath, String resultPath, String encoding) {
        this.outPath = outPath;
        this.resultPath = resultPath;
        this.encoding = encoding;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void start(List<Document> documents) {
        if (outPath == null || resultPath == null)
            throw new IllegalArgumentException("outPath cannot be null");
        try (MyFilePrinter filePrinter = new MyFilePrinter(resultPath, false, encoding)) {
            ArffLoader loader = new ArffLoader();
            loader.setFile(new File(outPath));

            Instances data = loader.getDataSet();
//            data.setClassIndex(data.numAttributes() - 1);
            String[] options = {"-N", "6", "-I", "100", "-V", "-output-debug-info"};
//            System.setOut(writer);
            EM em = new EM();
            em.setOptions(options);
            em.buildClusterer(data);
            filePrinter.write(em.toString());
            filePrinter.write(Arrays.toString(em.distributionForInstance(data.firstInstance())));
            filePrinter.close();
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MatrixHandler matrixHandler = new StandardMatrixHandler("/home/yjh/data/result/data_all.arff",
                "/home/yjh/data/result/result_all.txt", "utf8");
        matrixHandler.start(null);
    }
}
