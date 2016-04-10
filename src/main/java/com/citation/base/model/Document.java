package com.citation.base.model;

import com.citation.base.resource.MetaRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The represent of literature handled
 * Created by yjh on 16-4-4.
 */
public class Document {
    private String title;
    private String[] authorCN;
    private String authorEng;
    private String institution;
    private String abstract_;
    private String[] keywords;
    private String[] category;
    private String context;
    private String referenceContent;
    private String theme;
    private List<Reference> referenceList = new ArrayList<>();

    public void accept(MetaRule rule, String line) {
        if (line.contains(rule.getSign()))
            rule.doSet(this, line);
    }

    public void addReference(Reference reference) {
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
