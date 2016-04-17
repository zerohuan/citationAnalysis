package com.yjh.citation.anlysis;

import com.yjh.citation.base.AnalysisContext;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Token;
import com.yjh.citation.base.resource.Resource;

import java.util.*;

/**
 * Created by yjh on 16-4-8.
 */
public class BasicContext implements AnalysisContext {
    private int badDocCount;
    private int handledCount;
    private Map<String, Integer> tokenCounts = new TreeMap<>();
    private Map<String, Integer> titleCounts = new LinkedHashMap<>();
    private int titleId = 0;
    private List<Document> badDocs;
    private double[] ratios = new double[Token.Site.values().length];
    private int[] weights;
    private boolean isRefFWeight = false;
    private boolean[] isPartHasOrNot = new boolean[Token.Site.values().length];

    @Override
    public void reset() {
        badDocCount = 0;
        handledCount = 0;
        tokenCounts.clear();
        titleCounts.clear();
        titleId = 0;
        if (badDocs != null) badDocs.clear();
        isRefFWeight = false;
    }

    @Override
    public boolean[] isPartHasOrNot() {
        return isPartHasOrNot;
    }


    @Override
    public void setIsPartHasOrNot(boolean[] isPartHasOrNot) {
        this.isPartHasOrNot = isPartHasOrNot;
    }

    @Override
    public double[] getRatios() {
        return ratios;
    }

    @Override
    public void updateRatio(Token.Site site, double ratioValue) {
        ratios[site.ordinal()] = ratioValue;
    }

    @Override
    public int getTokenDF(String token) {
        Integer result;
        return tokenCounts == null ? 0 : ((result = tokenCounts.get(token)) == null ? 0 : result);
    }

    @Override
    public Map<String, Integer> getTokenMap() {
        return tokenCounts;
    }

    @Override
    public void addToken(String token) {
        if (tokenCounts.containsKey(token))
            tokenCounts.put(token, tokenCounts.get(token) + 1);
        else
            tokenCounts.put(token, 1);
    }

    @Override
    public int getTitleId(String title) {
        Integer result;
        return titleCounts == null ? 0 : ((result = titleCounts.get(title)) == null ? 0 : result);
    }

    @Override
    public Map<String, Integer> getTitleMap() {
        return titleCounts;
    }
    @Override
    public void addTitle(String title) {
        if (!titleCounts.containsKey(title))
            titleCounts.put(title, titleId++);
    }

    @Override
    public int getBadDocCount() {
        return badDocCount;
    }

    @Override
    public int addBadDocCount(Document document) {
        System.out.println(document);
        if (badDocs == null)
            badDocs = new ArrayList<>();
        badDocs.add(document);
        return ++badDocCount;
    }

    @Override
    public Resource getResource(String contextPath, String encoding, String metaPath) {
        return new Resource(contextPath, encoding, metaPath);
    }

    @Override
    public int addHandledDocCount(Document document) {
        return ++handledCount;
    }

    @Override
    public int getHandledDocCount() {
        return handledCount;
    }

    @Override
    public List<Document> getBadDocs() {
        return badDocs;
    }

    @Override
    public int[] getWeights() {
        return weights;
    }

    @Override
    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    public boolean isRefFWeight() {
        return isRefFWeight;
    }

    public void setIsRefFWeight(boolean isRefFWeight) {
        this.isRefFWeight = isRefFWeight;
    }

    public void setBadDocCount(int badDocCount) {
        this.badDocCount = badDocCount;
    }

    public void setBadDocs(List<Document> badDocs) {
        this.badDocs = badDocs;
    }

    public int getHandledCount() {
        return handledCount;
    }

    public void setHandledCount(int handledCount) {
        this.handledCount = handledCount;
    }

    public boolean[] getIsPartHasOrNot() {
        return isPartHasOrNot;
    }

    public void setRatios(double[] ratios) {
        this.ratios = ratios;
    }

    public Map<String, Integer> getTitleCounts() {
        return titleCounts;
    }

    public void setTitleCounts(Map<String, Integer> titleCounts) {
        this.titleCounts = titleCounts;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public Map<String, Integer> getTokenCounts() {
        return tokenCounts;
    }

    public void setTokenCounts(Map<String, Integer> tokenCounts) {
        this.tokenCounts = tokenCounts;
    }

    @Override
    public String toString() {
        return "BasicContext{" +
                "badDocCount=" + badDocCount +
                ", handledCount=" + handledCount +
                ", tokenCounts=" + tokenCounts.size() +
                ", titleId=" + titleId +
                '}';
    }
}
