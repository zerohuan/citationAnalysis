package com.yjh.citation.anlysis;

import com.yjh.citation.anlysis.generator.TokenAllMatrixGenerator;
import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.container.*;
import com.yjh.citation.base.resource.DocumentReader;
import com.yjh.citation.base.resource.MetaRule;
import com.yjh.util.StringUtil;

/**
 * Created by yjh on 16-4-7.
 */
public class BootStrap {
    private Container root;
    private AnalysisContext context = new BasicContext();

    public static final String dataPath = "/home/yjh/data/documents.txt";

    public void doAll() {
        //config
        Engine engine = new StandardEngine();
        engine.setReader(new DocumentReader(true, ".txt",
                new MetaRule.AuthorCNV(),
                new MetaRule.AuthorEng(),
                new MetaRule.Category(),
                new MetaRule.Institution(),
                new MetaRule.Keywords(),
                new MetaRule.Abstract()));
        engine.setResource(context.getResource(
                "/home/yjh/data/txt",
                "gbk",
                "/home/yjh/data/base"
        ));
        root = engine;

        Wrapper wrapper = new StandardWrapper();
        engine.addChild(wrapper);
    }

    public void config() {
        Engine engine = new StandardEngine();
        engine.setReader(new DocumentReader(true, ".txt",
                new MetaRule.AuthorCNV(),
                new MetaRule.AuthorEng(),
                new MetaRule.Category(),
                new MetaRule.Institution(),
                new MetaRule.Keywords(),
                new MetaRule.Abstract()));
        engine.setResource(context.getResource(
                "/home/yjh/data/txt",
                "gbk",
                "/home/yjh/data/base"
        ));
        root = engine;

        Wrapper wrapper = new StandardWrapper();
        engine.addChild(wrapper);
    }

    public void matrixHandleConfig() {


        StandardContainer container = new StandardContainer(dataPath, "utf8");
        root = container;

//        MatrixGenerator matrixGenerator =
//                new StandardMatrixGenerator(basePath + "data_ref.arff", "utf8",
//                        new CitationBoolMatrixGenerator());
//        matrixGenerator.setName("ref");
//        container.addChild(matrixGenerator);
//
//        MatrixHandler matrixHandler = new StandardMatrixHandler(basePath + "data_ref.arff",
//                basePath + "result_ref.txt", "utf8");
//        matrixHandler.setName("ref");
//        matrixGenerator.addChild(matrixHandler);
        StringBuilder suffixBuffer = new StringBuilder();
        boolean[] flags = context.isPartHasOrNot();
        for (int i = 0; i < flags.length; ++i) {
            if (!flags[i])
                suffixBuffer.append("no_").append(i + 1).append("_");
        }
        if (!context.isRefFWeight())
            suffixBuffer.append("no_");
        suffixBuffer.append("ref_weight_").append(StringUtil.showInts(context.getWeights()));
        String basePath = "/home/yjh/data/result/";
        MatrixGenerator matrixGeneratorAll =
                new StandardMatrixGenerator(basePath + "data_" + suffixBuffer.toString() + ".arff", "utf8",
                        new TokenAllMatrixGenerator());
        matrixGeneratorAll.setName("all");
        container.addChild(matrixGeneratorAll);

        MatrixHandler matrixHandlerAll = new StandardMatrixHandler(basePath + "data_" + suffixBuffer.toString() + "" +
                ".arff",
                basePath + "result_" + suffixBuffer.toString() + ".txt", "utf8");
        matrixHandlerAll.setName("all");
        matrixGeneratorAll.addChild(matrixHandlerAll);
    }

    public void doAnalysis() {
        root.init(context);

        root.start(null);
    }

    public void doGenerate() {
        for (boolean f : new boolean[]{false,true}) {
            for (boolean ff : new boolean[]{true,false}) {
                for (boolean fff : new boolean[]{true,false}) {
                    if (!fff && !ff)
                        break;
                    for (int i = 1; i <= 6; ++i) {
                        if (i > 1 && !ff)
                            break;
                        boolean[] flags = new boolean[]{true, true, f, ff};
                        context.setIsPartHasOrNot(flags);
                        context.setWeights(new int[]{4, 2, f ? 1 : 0, i});
                        context.setIsRefFWeight(ff && fff);
                        matrixHandleConfig();
                        doAnalysis();
                        context.reset();
                    }
                }
            }
        }

        for (boolean f : new boolean[]{true,false}) {
            for (int i = 1; i <= 6; ++i) {
                boolean[] flags = new boolean[]{false, false, false, true};
                context.setIsPartHasOrNot(flags);
                context.setWeights(new int[]{0, 0, 0, i});
                context.setIsRefFWeight(f);
                matrixHandleConfig();
                doAnalysis();
                context.reset();
            }
        }
    }

    public void doResolve() {
        config();
        doAnalysis();
    }

    public static void main(String[] args) throws Exception {
        BootStrap bootStrap = new BootStrap();
        bootStrap.doGenerate();
    }

}
