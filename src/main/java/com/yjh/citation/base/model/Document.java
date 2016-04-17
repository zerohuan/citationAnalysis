package com.yjh.citation.base.model;

import com.yjh.citation.base.resource.MetaRule;

import java.io.Serializable;
import java.util.*;

/**
 * The represent of literature handled
 * Created by yjh on 16-4-4.
 */
public class Document implements Serializable {
    private String title;
    private String[] authorCN;
    private String authorEng;
    private String institution;
    private String abstract_;
    private String[] keywords;
    private String[] category;
    private String context;
    private String referenceContent;
    private int theme;
    private List<Reference> referenceList = new ArrayList<>();
    private Map<String, Token> tokens = new HashMap<>();

    public void accept(MetaRule rule, String line) {
        if (line.contains(rule.getSign()))
            rule.doSet(this, line);
    }

    public void addToken(Token token) {
        if (tokens != null)
            tokens.put(token.getWord(), token);
    }

    public Token hasToken(String token) {
        return tokens.get(token);
    }

    public Map<String, Token> getTokens() {
        return tokens;
    }

    public void addReference(Reference reference) {
        if (referenceList != null)
            referenceList.add(reference);
    }

    public List<Reference> getReferenceList() {
        return referenceList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract_() {
        return abstract_;
    }

    public void setAbstract_(String abstract_) {
        this.abstract_ = abstract_;
    }

    public String[] getAuthorCN() {
        return authorCN;
    }

    public void setAuthorCN(String[] authorCN) {
        this.authorCN = authorCN;
    }

    public String getAuthorEng() {
        return authorEng;
    }

    public void setAuthorEng(String authorEng) {
        this.authorEng = authorEng;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getReferenceContent() {
        return referenceContent;
    }

    public void setReferenceContent(String referenceContent) {
        this.referenceContent = referenceContent;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    public void setTokens(Map<String, Token> tokens) {
        this.tokens = tokens;
    }

    public String printTokens() {
        StringBuilder buffer = new StringBuilder();
        for (Token token : tokens.values())
            buffer.append(token).append("\r\n");
        return buffer.toString();
    }

    @Override
    public String toString() {
        return "Document{" +
                "abstract_='" + abstract_ + '\'' +
                ", authorCN=" + Arrays.toString(authorCN) +
                ", authorEng='" + authorEng + '\'' +
                ", institution='" + institution + '\'' +
                ", keywords=" + Arrays.toString(keywords) +
                ", category=" + Arrays.toString(category) +
                ", referenceContent='" + referenceContent + '\'' +
                '}';
    }
}
