package com.yjh.citation.base;

import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Token;
import com.yjh.citation.base.resource.Resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The context info in whole analysis
 * Created by yjh on 16-4-8.
 */
public interface AnalysisContext extends Serializable {
    int[] getWeights();
    void setWeights(int[] weights);

    boolean isRefFWeight();
    void setIsRefFWeight(boolean isRefFWeight);

    boolean[] isPartHasOrNot();
    void setIsPartHasOrNot(boolean[] isPartHasOrNot);


    Resource getResource(String contextPath, String encoding, String metaPath);
    int getBadDocCount();
    int addBadDocCount(Document document);
    int getHandledDocCount();
    int addHandledDocCount(Document document);
    List<Document> getBadDocs();

    void addToken(String token);
    int getTokenDF(String token);
    Map<String, Integer> getTokenMap();

    void addTitle(String title);
    int getTitleId(String title);
    Map<String, Integer> getTitleMap();

    double[] getRatios();
    void updateRatio(Token.Site site, double ratioValue);

    void reset();
}
